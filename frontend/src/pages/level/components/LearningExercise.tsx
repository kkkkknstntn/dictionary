import { useCheckAnswer } from '@/hooks/api/learn.hooks'
import type { LearningMaterialDTO } from '@/shared/types/learn'
import { ArrowLeftOutlined, ReloadOutlined } from '@ant-design/icons'
import { Button, Grid } from 'antd'
import { useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { AudioPlayer } from './AudioPlayer'
import './LearningExercise.scss'

const { useBreakpoint } = Grid
const FEEDBACK_DELAY = 900 // время, пока подсветка видна (мс)

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

	const [selected, setSelected] = useState<string | null>(null)
	const [isCorrect, setIsCorrect] = useState<boolean | null>(null)

	const isAudioToWord = material?.type === 'AUDIO_TO_WORD'
	const isWordToImage = material?.type === 'WORD_TO_IMAGE'
	const isImageToWord = material?.type === 'IMAGE_TO_WORD'

	const audioPlayerRef = useRef<{ play: () => void; stop: () => void }>(null)

	const stopAudio = () => {
		audioPlayerRef.current?.stop()
	}

	const handleAnswer = (answer: string) => {
		if (!material || selected) return // если уже ответили, игнорируем

		stopAudio()
		setSelected(answer)

		checkAnswer(
			{ wordId: material.targetWord.id, answer, type: material.type },
			{
				onSuccess: res => {
					// СРАЗУ ставим правильный финальный isCorrect
					setIsCorrect(res.correct)

					setTimeout(() => {
						// сбрасываем после задержки
						setSelected(null)
						setIsCorrect(null)
						onNext()
					}, FEEDBACK_DELAY)
				},
			}
		)
	}

	const handleBack = () => {
		stopAudio()
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

			{isAudioToWord && (
				<AudioPlayer ref={audioPlayerRef} src={material?.targetWord.audioPath} />
			)}

			{isImageToWord && (
				<div className='target-wrapper'>
					<img
						src={material!.targetWord.imagePath}
						alt={material!.targetWord.word}
						className='target-image'
					/>
				</div>
			)}

			<div className={`options-grid ${screens.md ? 'desktop' : 'mobile'}`}>
	{material?.options.map(option => {
		let state // undefined, 'correct' или 'wrong'

		if (selected === option && isCorrect !== null) {
			state = isCorrect ? 'correct' : 'wrong'
		}

		return (
			<Button
				key={option}
				size='large'
				className='option-button'
				data-state={state}
				disabled={!!selected}
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
				<Button
					icon={<ReloadOutlined />}
					onClick={() => {
						stopAudio()
						onRestart()
					}}
				>
					Перезапустить уровень
				</Button>
			</div>
		</div>
	)
}
