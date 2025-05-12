import { useCourseProgressUsers } from '@/hooks/api/course.hooks'
import { List, Progress } from 'antd'

type Props = {
	courseId: number
}

export const CourseProgress = ({ courseId }: Props) => {
	const { data: progressData, isLoading } = useCourseProgressUsers(courseId)

	return (
		<List
			loading={isLoading}
			dataSource={progressData}
			renderItem={({ user, averageProgress }) => (
				<List.Item>
					<List.Item.Meta
						title={user.username}
						description={
							<Progress
								percent={averageProgress}
								status='active'
								strokeColor={{
									'0%': '#108ee9',
									'100%': '#87d068',
								}}
							/>
						}
					/>
				</List.Item>
			)}
		/>
	)
}
