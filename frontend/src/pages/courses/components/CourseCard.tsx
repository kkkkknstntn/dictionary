import type { CourseResponseDTO } from '@/shared/types/course'
import { Card, Progress, Tag, Typography } from 'antd'
import { Link } from 'react-router-dom'
import './CourseCard.scss'

const { Title, Paragraph } = Typography

interface Props {
	course: CourseResponseDTO
}

export const CourseCard = ({ course }: Props) => (
	<Card
		className='course-card'
		hoverable
		cover={
			/* Можно будет прикрутить картинку курса – сейчас только placeholder-цвет */
			<div className='cover' />
		}
		actions={[
			<Link key='open' to={`/course/${course.id}`}>
				Перейти
			</Link>,
		]}
	>
		<Title level={4} ellipsis={{ rows: 2 }}>
			{course.title}
		</Title>

		<Paragraph ellipsis={{ rows: 3 }}>{course.description}</Paragraph>

		<div className='meta'>
			<Tag color='blue'>Автор: {course.author.username}</Tag>
			<Tag color='geekblue'>Уровней: {course.levels.length}</Tag>
		</div>

		{/* Если back-end прислал aggregated progress для данного пользователя */}
		{course.progress !== undefined && (
			<Progress
				percent={course.progress}
				size='small'
				className='progress-bar'
			/>
		)}
	</Card>
)
