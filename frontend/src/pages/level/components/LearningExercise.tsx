import { useCheckAnswer } from '@/hooks/api/learn.hooks'
import type { LearningMaterialDTO } from '@/shared/types/learn'
import { Button, Grid, notification } from 'antd'
import './LearningExercise.scss'
import { AudioPlayer } from './AudioPlayer'

const { useBreakpoint } = Grid

type Props = {
	material?: LearningMaterialDTO
	onNext: () => void
}

export const LearningExercise = ({ material, onNext }: Props) => {
	const screens = useBreakpoint()
	const { mutate: checkAnswer } = useCheckAnswer()

	const handleAnswer = (answer: string) => {
		if (!material) return

		checkAnswer(
			{
				wordId: material.targetWord.id,
				answer,
				type: material.type,
			},
			{
				onSuccess: result => {
					notification[result.correct ? 'success' : 'error']({
						message: result.correct ? 'Правильно!' : 'Ошибка',
						description: result.correct
							? `Прогресс: +${result.newProgress}%`
							: `Правильный ответ: ${material.targetWord.word}`,
					})
					if (result.correct) onNext()
				},
			}
		)
	}

	return (
		<div className='exercise-container'>
			{material?.type === 'AUDIO_TO_WORD' && (
				<AudioPlayer src={material.targetWord.audioPath} />
			)}

			<div className={`options-grid ${screens.md ? 'desktop' : 'mobile'}`}>
				{material?.options.map(option => (
					<Button
						key={option}
						size='large'
						className='option-button'
						onClick={() => handleAnswer(option)}
					>
						{material.type === 'IMAGE_TO_WORD' ? (
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
