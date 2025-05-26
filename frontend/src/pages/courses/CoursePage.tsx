// CoursePage.tsx
import {
	useCourseDetails,
	useDeleteCourse,
	useUpdateCourse,
} from '@/hooks/api/course.hooks'
import { useCurrentUser } from '@/hooks/api/user.hooks'
import {
	BookOutlined,
	CloseOutlined,
	DeleteOutlined,
	EditOutlined,
	PlusOutlined,
	SaveOutlined,
	UploadOutlined,
} from '@ant-design/icons'
import {
  Avatar,
  Button,
  Card,
  Col,
  Input,
  message,
  Row,
  Skeleton,
  Tag,
  Tabs,      // <-- вот этот импорт добавь
  Typography,
} from 'antd'
import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import './CoursePage.scss'
import { AddLevelModal } from './components/AddLevelModal'
import { CourseProgress } from './components/CourseProgress'
import { CourseRating } from './components/CourseRating'
import { JoinCourseButton } from './components/JoinCourseButton'
import { LevelList } from './components/LevelList'
import { WordList } from './components/WordList'

const { Title, Paragraph } = Typography
const { TextArea } = Input

export const CoursePage = () => {
	const { id } = useParams<{ id: string }>()
	const { data: course, isLoading, refetch } = useCourseDetails(Number(id))
	const { data: currentUser } = useCurrentUser()
	const isAuthor = currentUser?.username === course?.author.username
	const [levelModalOpen, setLevelModalOpen] = useState(false)
	const [isEditing, setIsEditing] = useState(false)
	const [editedTitle, setEditedTitle] = useState('')
	const [editedDescription, setEditedDescription] = useState('')
	const [fileList, setFileList] = useState<File[]>([])
	const [previewUrl, setPreviewUrl] = useState<string | null>(null)
	const updateCourse = useUpdateCourse()
	const deleteCourse = useDeleteCourse()
	const navigate = useNavigate()

	const handleStartEditing = () => {
		setEditedTitle(course?.title || '')
		setEditedDescription(course?.description || '')
		setIsEditing(true)
		setPreviewUrl(null)
	}

	const handleCancelEditing = () => {
		setIsEditing(false)
		setFileList([])
		setPreviewUrl(null)
	}

	const handleSave = async () => {
		try {
			if (!editedTitle) {
				message.error('Название курса обязательно')
				return
			}

			await updateCourse.mutateAsync({
				id: Number(id),
				data: {
					title: editedTitle,
					description: editedDescription || undefined,
					imageFile: fileList[0] || undefined,
				},
			})
			setIsEditing(false)
			setFileList([])
			setPreviewUrl(null)
			message.success('Курс успешно обновлен')
		} catch (error) {
			message.error('Ошибка при обновлении курса')
		}
	}

	const handleDeleteCourse = async () => {
		if (window.confirm('Вы уверены, что хотите удалить этот курс?')) {
			await deleteCourse.mutateAsync(Number(id))
			navigate('/courses')
		}
	}

	const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		if (e.target.files && e.target.files[0]) {
			const file = e.target.files[0]
			setFileList([file])
			const url = URL.createObjectURL(file)
			setPreviewUrl(url)
		}
	}

	useEffect(() => {
		return () => {
			if (previewUrl) {
				URL.revokeObjectURL(previewUrl)
			}
		}
	}, [previewUrl])

	return (
		<div className='course-page'>
			{isLoading ? (
				<Skeleton active />
			) : (
				<Row gutter={24}>
					<Col xs={24} lg={18}>
						<Card className='course-header'>
							{/* Заменили Space на flex-контейнер */}
							<div className='course-flex-container'>
								<div className='course-avatar-container'>
									{isEditing ? (
										<label className='course-avatar-upload'>
											{previewUrl ? (
												<img src={previewUrl} alt='Preview' className='course-avatar' />
											) : course?.imagePath ? (
												<img src={course.imagePath} alt={course.title} className='course-avatar' />
											) : (
												<Avatar
													size={64}
													icon={<BookOutlined />}
													style={{ backgroundColor: '#FF6B35' }}
													className='course-avatar'
												/>
											)}
											<div className='course-avatar-overlay'>
												<UploadOutlined />
												<span>Изменить фото</span>
											</div>
											<input
												type='file'
												accept='image/*'
												onChange={handleFileChange}
												style={{ display: 'none' }}
											/>
										</label>
									) : course?.imagePath ? (
										<img src={course.imagePath} alt={course.title} className='course-avatar' />
									) : (
										<Avatar
											size={64}
											icon={<BookOutlined />}
											style={{ backgroundColor: '#FF6B35' }}
											className='course-avatar'
										/>
									)}
								</div>
								<div style={{ flex: 1 }}>
									<div className='course-title-row'>
										{isEditing ? (
											<Input
												value={editedTitle}
												onChange={e => setEditedTitle(e.target.value)}
												style={{ fontSize: '24px', fontWeight: 'bold' }}
											/>
										) : (
											<Title level={2} style={{ margin: 0 }}>
												{course?.title}
											</Title>
										)}
										<div className='course-actions'>
											{isAuthor && !isEditing && (
												<>
													<Button icon={<EditOutlined />} onClick={handleStartEditing} />
													<Button danger icon={<DeleteOutlined />} onClick={handleDeleteCourse} />
												</>
											)}
											{isEditing && (
												<>
													<Button
														type='primary'
														icon={<SaveOutlined />}
														onClick={handleSave}
														loading={updateCourse.isPending}
													/>
													<Button icon={<CloseOutlined />} onClick={handleCancelEditing} />
												</>
											)}
										</div>
									</div>

									{isEditing ? (
										<TextArea
											value={editedDescription}
											onChange={e => setEditedDescription(e.target.value)}
											rows={4}
										/>
									) : (
										<Paragraph>{course?.description}</Paragraph>
									)}

									<div className='course-meta'>
										<Tag color='blue' className='author-tag'>
											<span className='full-label'>Автор:</span> {course?.author.username}
										</Tag>
										<JoinCourseButton courseId={Number(id)} />
									</div>
								</div>
							</div>
						</Card>

						<Tabs defaultActiveKey='1' className='course-tabs'>
							<Tabs.TabPane tab='Уровни' key='1'>
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
								<LevelList
									levels={course?.levels || []}
									isAuthor={isAuthor}
									isEditing={isEditing}
								/>
								<AddLevelModal
									open={levelModalOpen}
									courseId={Number(id)}
									onClose={() => setLevelModalOpen(false)}
									onSuccess={() => refetch()}
								/>
							</Tabs.TabPane>

							<Tabs.TabPane tab='Слова' key='2'>
								{course?.levels.map(level => (
									<div key={level.id} className='level-words'>
										<Title level={3}>{level.name}</Title>
										<WordList levelId={level.id} />
									</div>
								))}
							</Tabs.TabPane>
						</Tabs>

						<Card className='course-progress-card' style={{ marginTop: 24 }}>
							<CourseProgress courseId={Number(id)} />
						</Card>
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
