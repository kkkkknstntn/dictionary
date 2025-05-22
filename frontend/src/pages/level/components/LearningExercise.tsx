import { useCheckAnswer } from '@/hooks/api/learn.hooks'
import type { LearningMaterialDTO } from '@/shared/types/learn'
import { ArrowLeftOutlined, ReloadOutlined } from '@ant-design/icons'
import { Button, Grid } from 'antd'
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { AudioPlayer } from './AudioPlayer'
import './LearningExercise.scss'

const { useBreakpoint } = Grid
const FEEDBACK_DELAY = 900 // - время, пока подсветка видна (мс)

interface Props {
	material?: LearningMaterialDTO
	onNext: () => void
	onRestart: () => void
	levelId: number
	setStarted: (started: boolean) => void
}

export const LearningExercise = ({
	material,
	onNext,
	onRestart,
	levelId,
	setStarted,
}: Props) => {
	const screens = useBreakpoint()
	const navigate = useNavigate()
	const { mutate: checkAnswer } = useCheckAnswer()

	/** выбранный пользователем вариант */
	const [selected, setSelected] = useState<string | null>(null)
	/** правильность последнего ответа */
	const [isCorrect, setIsCorrect] = useState<boolean | null>(null)

	const isAudioToWord = material?.type === 'AUDIO_TO_WORD'
	const isWordToImage = material?.type === 'WORD_TO_IMAGE'
	const isImageToWord = material?.type === 'IMAGE_TO_WORD'

	const handleAnswer = (answer: string) => {
		if (!material || selected) return // уже ответили

		setSelected(answer)

		checkAnswer(
			{ wordId: material.targetWord.id, answer, type: material.type },
			{
				onSuccess: res => {
					setIsCorrect(res.correct)

					// ждём, чтобы пользователь увидел цвет/подсказку, и берём след. вопрос
					setTimeout(() => {
						setSelected(null)
						setIsCorrect(null)
						onNext()
					}, FEEDBACK_DELAY)
				},
			}
		)
	}

	const handleBack = () => {
		setStarted(false)
		navigate(`/level/${levelId}`)
	}

	return (
		<div className='exercise-container'>
			<div className='exercise-header'>
				<Button icon={<ArrowLeftOutlined />} onClick={handleBack}>
					Вернуться к уровню
				</Button>
			</div>

			{/* 1. Аудио-подсказка (AUDIO_TO_WORD) */}
			{isAudioToWord && <AudioPlayer src={material?.targetWord.audioPath} />}

			{/* 2. Целевая картинка (IMAGE_TO_WORD) */}
			{isImageToWord && (
				<div className='target-wrapper'>
					<img
						src={material!.targetWord.imagePath}
						alt={material!.targetWord.word}
						className='target-image'
					/>
				</div>
			)}

			{/* 3. Варианты ответа */}
			<div className={`options-grid ${screens.md ? 'desktop' : 'mobile'}`}>
				{material?.options.map(option => {
					/** подсвечиваем только выбранную кнопку */
					let state: 'correct' | 'wrong' | undefined
					if (selected && option === selected) {
						state = isCorrect ? 'correct' : 'wrong'
					}

					return (
						<Button
							key={option}
							size='large'
							className='option-button'
							data-state={state}
							disabled={!!selected} // блокируем клики, пока показываем фидбек
							onClick={() => handleAnswer(option)}
						>
							{isWordToImage ? (
								<img src={option} alt='Вариант' className='image-option' />
							) : (
								option
							)}
						</Button>
					)
				})}
			</div>

			<div className='exercise-footer'>
				<Button icon={<ReloadOutlined />} onClick={onRestart}>
					Перезапустить уровень
				</Button>
			</div>
		</div>
	)
}
