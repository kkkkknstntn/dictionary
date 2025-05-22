import { useWordProgress } from '@/hooks/api/progress.hooks'
import { useWordDetails, useWordNavigation } from '@/hooks/api/word.hooks'
import { ArrowLeftOutlined, ArrowRightOutlined } from '@ant-design/icons'
import { Button, Card, Progress, Space, Typography } from 'antd'
import { useNavigate, useParams } from 'react-router-dom'
import './WordPage.scss'

const { Title, Text } = Typography

export const WordPage = () => {
	const { id } = useParams<{ id: string }>()
	const navigate = useNavigate()
	const { data: word, isLoading } = useWordDetails(Number(id))
	const { data: progress } = useWordProgress(Number(id))
	const { canGoNext, canGoPrevious, goToNextWord, goToPreviousWord } =
		useWordNavigation(Number(id), word?.levelId ?? 0)

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
						</Space>
					</div>

					<div className='word-info'>
						<div className='word-section'>
							<Title level={4} className='section-title'>
								Слово
							</Title>
							<Title level={2}>{word.word}</Title>
						</div>

						<div className='definition-section'>
							<Title level={4} className='section-title'>
								Определение
							</Title>
							<Text type='secondary'>{word.definition}</Text>
						</div>
					</div>

					<div className='media-section'>
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
