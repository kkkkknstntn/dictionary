import { useCourses } from '@/hooks/api/course.hooks'
import { useCurrentUser } from '@/hooks/api/user.hooks'
import { Button, Col, Empty, Row, Skeleton, Typography } from 'antd'
import { useEffect, useState } from 'react'
import { AddCourseModal } from './components/AddCourseModal'
import { CourseCard } from './components/CourseCard'
import './CoursesPage.scss'

const { Title } = Typography

export const CoursesPage = () => {
	const {
		data: courses = [],
		isLoading,
		isError,
		error,
		refetch,
	} = useCourses()

	const { data: currentUser } = useCurrentUser()
	const [modalOpen, setModalOpen] = useState(false)

	useEffect(() => {
		// При фокусе возвращаем свежие данные
		const handler = () => refetch()
		window.addEventListener('focus', handler)
		return () => window.removeEventListener('focus', handler)
	}, [refetch])

	if (isLoading) {
		return <Skeleton active paragraph={{ rows: 8 }} />
	}

	if (isError) {
		return (
			<Empty description={error instanceof Error ? error.message : 'Ошибка'} />
		)
	}

	return (
		<div className='courses-page'>
			<Title level={2}>Все курсы</Title>
			{currentUser && (
				<Button
					type='primary'
					//icon={<PlusOutlined />}
					onClick={() => setModalOpen(true)}
					style={{ marginBottom: 24 }}
				>
					Создать курс
				</Button>
			)}
			{courses.length === 0 ? (
				<Empty description='Курсы не найдены' />
			) : (
				<Row gutter={[24, 24]}>
					{courses.map(course => (
						<Col xs={24} sm={12} md={8} lg={6} key={course.id}>
							<CourseCard course={course} />
						</Col>
					))}
				</Row>
			)}
			<AddCourseModal open={modalOpen} onClose={() => setModalOpen(false)} />
		</div>
	)
}
