import { useCourseProgress } from '@/hooks/api/course.hooks'
import { ProgressAverageDTO } from '@/shared/types/progress'
import { TrophyOutlined } from '@ant-design/icons'
import { Card, List, Skeleton, Typography } from 'antd'

interface CourseRatingProps {
	courseId: number | undefined
}

export const CourseRating = ({ courseId }: CourseRatingProps) => {
	const { data: progress, isLoading } = useCourseProgress(courseId)

	if (!courseId) {
		return null
	}

	if (isLoading) {
		return <Skeleton active />
	}

	const sortedProgress = [...(progress || [])].sort(
		(a, b) => b.averageProgress - a.averageProgress
	)

	return (
		<Card title='Рейтинг пользователей' className='course-rating'>
			<List
				dataSource={sortedProgress}
				renderItem={(item: ProgressAverageDTO, index) => (
					<List.Item>
						<List.Item.Meta
							avatar={
								<div className='rating-position'>
									{index < 3 ? (
										<TrophyOutlined
											style={{
												fontSize: 24,
												color:
													index === 0
														? '#FFD700'
														: index === 1
														? '#C0C0C0'
														: '#CD7F32',
											}}
										/>
									) : (
										<Typography.Text strong>{index + 1}</Typography.Text>
									)}
								</div>
							}
							title={item.user.username}
							description={`Прогресс: ${Math.round(item.averageProgress)}%`}
						/>
					</List.Item>
				)}
			/>
		</Card>
	)
}
