import { useCheckAnswer } from '@/hooks/api/learn.hooks'
import type { LearningMaterialDTO } from '@/shared/types/learn'
import { Button, Grid, notification } from 'antd'
import { AudioPlayer } from './AudioPlayer'
import './LearningExercise.scss'

const { useBreakpoint } = Grid

type Props = {
	material?: LearningMaterialDTO
	onNext: () => void
}

export const LearningExercise = ({ material, onNext }: Props) => {
	const screens = useBreakpoint()
	const { mutate: checkAnswer } = useCheckAnswer()

	// удобные флаги — читаемее, чем сравнивать строку в JSX
	const isAudioToWord = material?.type === 'AUDIO_TO_WORD'
	const isWordToImage = material?.type === 'WORD_TO_IMAGE'
	const isImageToWord = material?.type === 'IMAGE_TO_WORD'

	const handleAnswer = (answer: string) => {
		if (!material) return

		checkAnswer(
			{ wordId: material.targetWord.id, answer, type: material.type },
			{
				onSuccess: result => {
					notification[result.isCorrect ? 'success' : 'error']({
						message: result.isCorrect ? 'Правильно!' : 'Ошибка',
						description: result.isCorrect
							? `Прогресс: +${result.newProgress}%`
							: `Правильный ответ: ${material.targetWord.word}`,
					})
					if (result.isCorrect) onNext()
				},
			}
		)
	}

	return (
		<div className='exercise-container'>
			{/* 1. Аудио — только для AUDIO_TO_WORD */}
			{isAudioToWord && <AudioPlayer src={material!.targetWord.audioPath} />}

			{/* 2. Изображение цели — только для IMAGE_TO_WORD */}
			{isImageToWord && (
				<div className='target-wrapper'>
					<img
						src={material!.targetWord.imagePath}
						alt={material!.targetWord.word}
						className='target-image'
					/>
				</div>
			)}

			{/* 3. Сетка вариантов */}
			<div className={`options-grid ${screens.md ? 'desktop' : 'mobile'}`}>
				{material?.options.map(option => (
					<Button
						key={option}
						size='large'
						className='option-button'
						onClick={() => handleAnswer(option)}
					>
						{isWordToImage ? (
							<img src={option} alt='Вариант' className='image-option' />
						) : (
							option
						)}
					</Button>
				))}
			</div>
		</div>
	)
}
