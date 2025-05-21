import { useCourseDetails } from '@/hooks/api/course.hooks'
import { Button, Card, Skeleton, Tabs, Tag, Typography } from 'antd'
import { useParams } from 'react-router-dom'

import { useCurrentUser } from '@/hooks/api/user.hooks'
import { PlusOutlined } from '@ant-design/icons'
import { useState } from 'react'
import './CoursePage.scss'
import { AddLevelModal } from './components/AddLevelModal'
import { CourseProgress } from './components/CourseProgress'
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
				<>
					<Card className='course-header'>
						<Title level={2}>{course?.title}</Title>
						<Paragraph>{course?.description}</Paragraph>

						<div className='course-meta'>
							<Tag color='blue'>Автор: {course?.author.username}</Tag>
							<JoinCourseButton courseId={Number(id)} />
						</div>
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
							<WordList levels={course?.levels || []} />
						</TabPane>

						<TabPane tab='Прогресс' key='3'>
							<CourseProgress courseId={Number(id)} />
						</TabPane>
					</Tabs>
				</>
			)}
		</div>
	)
}
