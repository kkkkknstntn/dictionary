import { useUserCourses } from '@/hooks/api/course.hooks'
import { List, Progress, Tag, Typography } from 'antd'
import { Link } from 'react-router-dom'

const { Text } = Typography

export const UserCourses = () => {
	const { data: courses, isPending } = useUserCourses()

	return (
		<List
			loading={isPending}
			itemLayout='vertical'
			dataSource={courses}
			renderItem={course => (
				<List.Item
					actions={[
						<Link key='open' to={`/course/${course.id}`}>
							Открыть курс
						</Link>,
					]}
				>
					<List.Item.Meta
						title={<Text strong>{course.title}</Text>}
						description={
							<div className='course-progress'>
								<Progress
									percent={course.progress || 0}
									status='active'
									strokeColor={{
										'0%': '#108ee9',
										'100%': '#87d068',
									}}
								/>
								<Tag color='geekblue'>Уровней: {course.levels?.length}</Tag>
							</div>
						}
					/>
				</List.Item>
			)}
		/>
	)
}
