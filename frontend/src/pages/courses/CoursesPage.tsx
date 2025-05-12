import { useCourses } from '@/hooks/api/course.hooks'
import { Col, Empty, Row, Skeleton, Typography } from 'antd'
import { useEffect } from 'react'
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
		return <Empty description={error instanceof Error ? error.message : 'Ошибка'} />
	}

	return (
		<div className='courses-page'>
			<Title level={2}>Все курсы</Title>

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
		</div>
	)
}
