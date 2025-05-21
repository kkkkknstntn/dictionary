import { useUserCourses } from '@/hooks/api/course.hooks'
import { useCourseProgress } from '@/hooks/api/progress.hooks'
import { List, Progress, Skeleton, Tag, Typography } from 'antd'
import { Link } from 'react-router-dom'

const { Text } = Typography

interface CourseItemProps {
	course: {
		id: number
		title: string
		levels?: { length: number }
	}
}

const CourseItem = ({ course }: CourseItemProps) => {
	const { data: progress, isLoading } = useCourseProgress(course.id)
	const progressPercent = Math.round(progress?.averageProgress || 0)

	return (
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
						{isLoading ? (
							<Skeleton.Input active size='small' />
						) : (
							<Progress
								percent={progressPercent}
								status='active'
								strokeColor={{
									'0%': '#108ee9',
									'100%': '#87d068',
								}}
							/>
						)}
						<Tag color='geekblue'>Уровней: {course.levels?.length}</Tag>
					</div>
				}
			/>
		</List.Item>
	)
}

export const UserCourses = () => {
	const { data: courses, isPending } = useUserCourses()

	return (
		<List
			loading={isPending}
			itemLayout='vertical'
			dataSource={courses}
			renderItem={course => <CourseItem course={course} />}
		/>
	)
}
