import { useCourses, useSearchCourses } from '@/hooks/api/course.hooks'
import { useCurrentUser } from '@/hooks/api/user.hooks'
import { useDebounce } from '@/hooks/useDebounce'
import { PlusOutlined, SearchOutlined } from '@ant-design/icons'
import { Button, Col, Empty, Input, Row, Skeleton, Typography } from 'antd'
import { useEffect, useState } from 'react'
import { AddCourseModal } from './components/AddCourseModal'
import { CourseCard } from './components/CourseCard'
import './CoursesPage.scss'

const { Title } = Typography

export const CoursesPage = () => {
	const [searchQuery, setSearchQuery] = useState('')
	const debouncedSearchQuery = useDebounce(searchQuery, 500)

	const {
		data: courses = [],
		isLoading,
		isError,
		error,
		refetch,
	} = useCourses()

	const {
		data: searchResults,
		isLoading: isSearchLoading,
		isError: isSearchError,
		error: searchError,
	} = useSearchCourses(debouncedSearchQuery)

	const { data: currentUser } = useCurrentUser()
	const [modalOpen, setModalOpen] = useState(false)

	useEffect(() => {
		// При фокусе возвращаем свежие данные
		const handler = () => refetch()
		window.addEventListener('focus', handler)
		return () => window.removeEventListener('focus', handler)
	}, [refetch])

	const displayCourses = debouncedSearchQuery ? searchResults ?? [] : courses
	const isLoadingDisplay = debouncedSearchQuery ? isSearchLoading : isLoading
	const isErrorDisplay = debouncedSearchQuery ? isSearchError : isError
	const errorDisplay = debouncedSearchQuery ? searchError : error

	if (isErrorDisplay) {
		return (
			<Empty
				description={
					errorDisplay instanceof Error
						? errorDisplay.message
						: 'Произошла ошибка при загрузке курсов'
				}
			/>
		)
	}

	return (
		<div className='courses-page'>
			<div className='courses-header'>
				<Title level={2}>Все курсы</Title>
				<div className='courses-actions'>
					<Input
						placeholder='Поиск курсов...'
						prefix={<SearchOutlined />}
						value={searchQuery}
						onChange={e => setSearchQuery(e.target.value)}
						allowClear
						className='search-input'
					/>
					{currentUser && (
						<Button
							type='primary'
							icon={<PlusOutlined />}
							onClick={() => setModalOpen(true)}
						>
							Создать курс
						</Button>
					)}
				</div>
			</div>

			{isLoadingDisplay ? (
				<Skeleton active paragraph={{ rows: 8 }} />
			) : displayCourses.length === 0 ? (
				<Empty
					description={
						debouncedSearchQuery
							? 'По вашему запросу ничего не найдено'
							: 'Курсы не найдены'
					}
				/>
			) : (
				<Row gutter={[24, 24]}>
					{displayCourses.map(course => (
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
