// CoursePage.tsx
import { useCourseDetails } from '@/hooks/api/course.hooks'
import { useCurrentUser } from '@/hooks/api/user.hooks'
import { BookOutlined, PlusOutlined } from '@ant-design/icons'
import {
	Avatar,
	Button,
	Card,
	Col,
	Row,
	Skeleton,
	Space,
	Tabs,
	Tag,
	Typography,
} from 'antd'
import { useState } from 'react'
import { useParams } from 'react-router-dom'
import './CoursePage.scss'
import { AddLevelModal } from './components/AddLevelModal'
import { CourseProgress } from './components/CourseProgress'
import { CourseRating } from './components/CourseRating'
import { JoinCourseButton } from './components/JoinCourseButton'
import { LevelList } from './components/LevelList'
import { WordList } from './components/WordList'

const { Title, Paragraph } = Typography
const { TabPane } = Tabs

export const CoursePage = () => {
	const { id } = useParams<{ id: string }>()
	const { data: course, isLoading } = useCourseDetails(Number(id))
	const { data: currentUser } = useCurrentUser()
	const isAuthor = currentUser?.username === course?.author.username
	const [levelModalOpen, setLevelModalOpen] = useState(false)

	return (
		<div className='course-page'>
			{isLoading ? (
				<Skeleton active />
			) : (
				<Row gutter={24}>
					<Col xs={24} lg={18}>
						<Card className='course-header'>
							<Space align='start' size='large'>
								{course?.imagePath ? (
									<img
										src={course.imagePath}
										alt={course.title}
										className='course-avatar'
									/>
								) : (
									<Avatar
										size={64}
										icon={<BookOutlined />}
										style={{ backgroundColor: '#FF6B35' }}
										className='course-avatar'
									/>
								)}
								<div>
									<Title level={2}>{course?.title}</Title>
									<Paragraph>{course?.description}</Paragraph>

									<div className='course-meta'>
										<Tag color='blue'>Автор: {course?.author.username}</Tag>
										<JoinCourseButton courseId={Number(id)} />
									</div>
								</div>
							</Space>
						</Card>

						<Card className='course-progress-card'>
							<CourseProgress courseId={Number(id)} />
						</Card>

						<Tabs defaultActiveKey='1' className='course-tabs'>
							<TabPane tab='Уровни' key='1'>
								{isAuthor && (
									<Button
										type='dashed'
										icon={<PlusOutlined />}
										style={{ marginBottom: 16 }}
										onClick={() => setLevelModalOpen(true)}
									>
										Добавить уровень
									</Button>
								)}
								<LevelList levels={course?.levels || []} />
								<AddLevelModal
									open={levelModalOpen}
									courseId={Number(id)}
									onClose={() => setLevelModalOpen(false)}
								/>
							</TabPane>

							<TabPane tab='Слова' key='2'>
								{isAuthor && (
									<Button
										type='dashed'
										icon={<PlusOutlined />}
										style={{ marginBottom: 16 }}
										onClick={() => setLevelModalOpen(true)}
									>
										Добавить слово
									</Button>
								)}
								{course?.levels.map(level => (
									<div key={level.id} className='level-words'>
										<Title level={3}>{level.name}</Title>
										<WordList levelId={level.id} />
									</div>
								))}
							</TabPane>
						</Tabs>
					</Col>

					<Col xs={24} lg={6}>
						<Card className='course-rating-card'>
							<Title level={4}>Топ пользователей</Title>
							<CourseRating courseId={Number(id)} />
						</Card>
					</Col>
				</Row>
			)}
		</div>
	)
}
