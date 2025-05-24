import { useWordProgress } from '@/hooks/api/progress.hooks'
import {
	useDeleteWord,
	useUpdateWord,
	useWordDetails,
	useWordNavigation,
} from '@/hooks/api/word.hooks'
import {
	ArrowLeftOutlined,
	ArrowRightOutlined,
	CloseOutlined,
	DeleteOutlined,
	EditOutlined,
	SaveOutlined,
	UploadOutlined,
} from '@ant-design/icons'
import { Button, Card, Input, message, Progress, Space, Typography } from 'antd'
import { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import './WordPage.scss'

const { Title, Text } = Typography
const { TextArea } = Input

export const WordPage = () => {
	const { id } = useParams<{ id: string }>()
	const navigate = useNavigate()
	const { data: word, isLoading } = useWordDetails(Number(id))
	const { data: progress } = useWordProgress(Number(id))
	const { canGoNext, canGoPrevious, goToNextWord, goToPreviousWord } =
		useWordNavigation(Number(id), word?.levelId ?? 0)
	const [isEditing, setIsEditing] = useState(false)
	const [editedWord, setEditedWord] = useState('')
	const [editedDefinition, setEditedDefinition] = useState('')
	const [imageFile, setImageFile] = useState<File | null>(null)
	const [audioFile, setAudioFile] = useState<File | null>(null)
	const [videoFile, setVideoFile] = useState<File | null>(null)
	const [previewUrl, setPreviewUrl] = useState<string | null>(null)
	const updateWord = useUpdateWord()
	const deleteWord = useDeleteWord()

	const handleStartEditing = () => {
		if (word) {
			setEditedWord(word.word)
			setEditedDefinition(word.definition)
			setIsEditing(true)
		}
	}

	const handleCancelEditing = () => {
		setIsEditing(false)
		setImageFile(null)
		setAudioFile(null)
		setVideoFile(null)
		setPreviewUrl(null)
		if (word) {
			setEditedWord(word.word)
			setEditedDefinition(word.definition)
		}
	}

	const handleSave = async () => {
		if (!word) return

		try {
			const formData = new FormData()
			formData.append('word', editedWord)
			formData.append('definition', editedDefinition)
			formData.append('levelId', word.levelId.toString())
			formData.append('activeForTesting', 'true')

			if (imageFile) {
				formData.append('imageFile', imageFile)
			}
			if (audioFile) {
				formData.append('audioFile', audioFile)
			}
			if (videoFile) {
				formData.append('videoFile', videoFile)
			}

			await updateWord.mutateAsync({
				id: word.id,
				formData,
			})
			setIsEditing(false)
			setImageFile(null)
			setAudioFile(null)
		} catch {
			message.error('Ошибка при обновлении слова')
		}
	}

	const handleDeleteWord = async () => {
		if (!word) return

		if (window.confirm('Вы уверены, что хотите удалить это слово?')) {
			try {
				await deleteWord.mutateAsync(word.id)
				navigate(`/level/${word.levelId}`)
			} catch {
				message.error('Ошибка при удалении слова')
			}
		}
	}

	const handleFileChange = (
		e: React.ChangeEvent<HTMLInputElement>,
		type: 'image' | 'audio' | 'video'
	) => {
		if (e.target.files && e.target.files[0]) {
			const file = e.target.files[0]
			if (type === 'image') {
				setImageFile(file)
				const url = URL.createObjectURL(file)
				setPreviewUrl(url)
			} else if (type === 'audio') {
				setAudioFile(file)
			} else if (type === 'video') {
				setVideoFile(file)
			}
		}
	}

	const handleBack = () => {
		if (word?.courseId) {
			navigate(`/course/${word.courseId}`)
		} else {
			navigate(-1)
		}
	}

	const handleBackToLevel = () => {
		if (word?.levelId) {
			navigate(`/level/${word.levelId}`)
		}
	}

	if (isLoading) {
		return <div>Загрузка...</div>
	}

	if (!word) {
		return <div>Слово не найдено</div>
	}

	return (
		<div className='word-page'>
			<Card className='word-content'>
				<Space direction='vertical' size='large' style={{ width: '100%' }}>
					<div className='word-header'>
						<Space>
							<Button icon={<ArrowLeftOutlined />} onClick={handleBack}>
								Вернуться к курсу
							</Button>
							<Button icon={<ArrowLeftOutlined />} onClick={handleBackToLevel}>
								Вернуться к уровню
							</Button>
						</Space>
						<Space>
							{!isEditing ? (
								<>
									<Button
										icon={<ArrowLeftOutlined />}
										onClick={goToPreviousWord}
										disabled={!canGoPrevious}
									>
										Назад
									</Button>
									<Button
										icon={<ArrowRightOutlined />}
										onClick={goToNextWord}
										disabled={!canGoNext}
									>
										Далее
									</Button>
									<Button
										icon={<EditOutlined />}
										onClick={handleStartEditing}
									/>
									<Button
										danger
										icon={<DeleteOutlined />}
										onClick={handleDeleteWord}
									/>
								</>
							) : (
								<>
									<Button
										type='primary'
										icon={<SaveOutlined />}
										onClick={handleSave}
										loading={updateWord.isPending}
									/>
									<Button
										icon={<CloseOutlined />}
										onClick={handleCancelEditing}
									/>
								</>
							)}
						</Space>
					</div>

					<div className='word-info'>
						<div className='word-section'>
							<Title level={4} className='section-title'>
								Слово
							</Title>
							{isEditing ? (
								<Input
									value={editedWord}
									onChange={e => setEditedWord(e.target.value)}
									style={{ fontSize: '24px', fontWeight: 'bold' }}
								/>
							) : (
								<Title level={2}>{word.word}</Title>
							)}
						</div>

						<div className='definition-section'>
							<Title level={4} className='section-title'>
								Определение
							</Title>
							{isEditing ? (
								<TextArea
									value={editedDefinition}
									onChange={e => setEditedDefinition(e.target.value)}
									rows={4}
								/>
							) : (
								<Text type='secondary'>{word.definition}</Text>
							)}
						</div>
					</div>

					<div className='media-section'>
						{isEditing ? (
							<>
								<div className='media-item'>
									<Title level={4} className='section-title'>
										Изображение
									</Title>
									<div className='media-content'>
										{previewUrl ? (
											<img
												src={previewUrl}
												alt='Preview'
												className='word-image'
											/>
										) : word.imagePath ? (
											<img
												src={word.imagePath}
												alt={word.word}
												className='word-image'
											/>
										) : (
											<div className='upload-placeholder'>
												<UploadOutlined />
												<span>Загрузить изображение</span>
											</div>
										)}
									</div>
									<div className='upload-button-row'>
										<input
											type='file'
											accept='image/*'
											onChange={e => handleFileChange(e, 'image')}
											id='image-upload'
											style={{ display: 'none' }}
										/>
										<Button
											icon={<UploadOutlined />}
											size='large'
											type='default'
											className='upload-button'
											onClick={() =>
												document.getElementById('image-upload')?.click()
											}
										>
											Загрузить новое изображение
										</Button>
									</div>
								</div>

								<div className='media-item'>
									<Title level={4} className='section-title'>
										Аудио
									</Title>
									<div className='media-content'>
										{word.audioPath ? (
											<audio controls src={word.audioPath}>
												Ваш браузер не поддерживает аудио элемент.
											</audio>
										) : (
											<div className='upload-placeholder'>
												<UploadOutlined />
												<span>Загрузить аудио</span>
											</div>
										)}
									</div>
									<div className='upload-button-row'>
										<input
											type='file'
											accept='audio/*'
											onChange={e => handleFileChange(e, 'audio')}
											id='audio-upload'
											style={{ display: 'none' }}
										/>
										<Button
											icon={<UploadOutlined />}
											size='large'
											type='default'
											className='upload-button'
											onClick={() =>
												document.getElementById('audio-upload')?.click()
											}
										>
											Загрузить новое аудио
										</Button>
									</div>
								</div>

								<div className='media-item'>
									<Title level={4} className='section-title'>
										Видео
									</Title>
									<div className='media-content'>
										{word.videoPath ? (
											<video controls src={word.videoPath}>
												Ваш браузер не поддерживает видео элемент.
											</video>
										) : (
											<div className='upload-placeholder'>
												<UploadOutlined />
												<span>Загрузить видео</span>
											</div>
										)}
									</div>
									<div className='upload-button-row'>
										<input
											type='file'
											accept='video/*'
											onChange={e => handleFileChange(e, 'video')}
											id='video-upload'
											style={{ display: 'none' }}
										/>
										<Button
											icon={<UploadOutlined />}
											size='large'
											type='default'
											className='upload-button'
											onClick={() =>
												document.getElementById('video-upload')?.click()
											}
										>
											Загрузить новое видео
										</Button>
									</div>
								</div>
							</>
						) : (
							<>
								{word.imagePath && (
									<div className='media-item'>
										<Title level={4} className='section-title'>
											Изображение
										</Title>
										<img
											src={word.imagePath}
											alt={word.word}
											className='word-image'
										/>
									</div>
								)}

								{word.audioPath && (
									<div className='media-item'>
										<Title level={4} className='section-title'>
											Аудио
										</Title>
										<audio controls src={word.audioPath}>
											Ваш браузер не поддерживает аудио элемент.
										</audio>
									</div>
								)}

								{word.videoPath && (
									<div className='media-item'>
										<Title level={4} className='section-title'>
											Видео
										</Title>
										<video controls src={word.videoPath}>
											Ваш браузер не поддерживает видео элемент.
										</video>
									</div>
								)}
							</>
						)}
					</div>

					{progress && (
						<div className='progress-section'>
							<Title level={4} className='section-title'>
								Ваш прогресс
							</Title>
							<Progress percent={progress.averageProgress} />
						</div>
					)}
				</Space>
			</Card>
		</div>
	)
}
