import { useCourseProgressUsers } from '@/hooks/api/course.hooks'
import { ProgressAverageDTO } from '@/shared/types/progress'
import { TrophyOutlined } from '@ant-design/icons'
import { List, Skeleton, Typography } from 'antd'

interface CourseRatingProps {
	courseId: number | undefined
}

export const CourseRating = ({ courseId }: CourseRatingProps) => {
	const { data: progressData, isLoading } = useCourseProgressUsers(courseId)

	if (!courseId) {
		return null
	}

	if (isLoading) {
		return <Skeleton active />
	}

	const sortedProgress = [...(progressData || [])].sort(
		(a, b) => b.averageProgress - a.averageProgress
	)

	return (
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
						description={`${Math.round(item.averageProgress)}%`}
					/>
				</List.Item>
			)}
		/>
	)
}
