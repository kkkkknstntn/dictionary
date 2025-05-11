import { useLearningMaterial } from '@/hooks/api/learn.hooks'
import { Card, Skeleton, Typography } from 'antd'
import { useParams } from 'react-router-dom'
import './LevelPage.scss'
import { LearningExercise } from './components/LearningExercise'

const { Title } = Typography

export const LevelPage = () => {
	const { id } = useParams<{ id: string }>()
	const {
		data: material,
		isLoading,
		refetch,
	} = useLearningMaterial({
		levelId: Number(id),
		type: 'WORD_TO_IMAGE', // Default type
	})

	return (
		<div className='level-page'>
			<Card className='learning-card'>
				{isLoading ? (
					<Skeleton active />
				) : (
					<>
						<Title level={3} className='exercise-title'>
							{material?.targetWord.word}
						</Title>

						<LearningExercise material={material} onNext={() => refetch()} />
					</>
				)}
			</Card>
		</div>
	)
}
