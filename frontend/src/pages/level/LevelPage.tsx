import { queryClient } from '@/app/QueryProvider'
import { useLearningMaterial } from '@/hooks/api/learn.hooks'
import { useIsCourseAuthor } from '@/hooks/api/level-extra.hooks'
import { useLevelProgress, useWordProgress } from '@/hooks/api/progress.hooks'
import { useCurrentUser } from '@/hooks/api/user.hooks'
import { useLevelDetails, useWordsByLevel } from '@/hooks/api/word.hooks'
import { QUERY_KEYS } from '@/shared/constants/queryKeys'
import type { LearningType } from '@/shared/types/learn'
import { PlusOutlined } from '@ant-design/icons'
import {
	Button,
	Card,
	Col,
	List,
	Progress,
	Row,
	Select,
	Skeleton,
	Typography,
} from 'antd'
import { useState } from 'react'
import { useParams } from 'react-router-dom'
import { LearningExercise } from './components/LearningExercise'
import { WordFormModal } from './components/WordFormModal'
import './LevelPage.scss'

const { Title, Paragraph, Text } = Typography

const learningModes = [
	{
		value: 'WORD_TO_IMAGE',
		label: 'Слово → картинка',
		description:
			'Вам будет показано слово, и вы должны выбрать правильную картинку из предложенных вариантов. Этот режим помогает закрепить визуальные ассоциации со словами.',
	},
	{
		value: 'IMAGE_TO_WORD',
		label: 'Картинка → слово',
		description:
			'Вам будет показана картинка, и вы должны написать соответствующее ей слово. Этот режим тренирует активный словарный запас и правописание.',
	},
	{
		value: 'AUDIO_TO_WORD',
		label: 'Аудио → слово',
		description:
			'Вам будет проиграно аудио со словом, и вы должны написать его. Этот режим развивает навыки аудирования и правописания.',
	},
]

export const LevelPage = () => {
	const { id } = useParams<{ id: string }>()
	const levelId = Number(id)
	const { data: currentUser } = useCurrentUser()
	const [wordModal, setWordModal] = useState(false)
	const { data: isAuthor } = useIsCourseAuthor(levelId, currentUser?.username)
	const [started, setStarted] = useState(false)
	const [type, setType] = useState<LearningType>('WORD_TO_IMAGE')
	const { data: words, isLoading: wordsLoading } = useWordsByLevel(levelId)
	const { data: progress } = useLevelProgress(levelId)
	const { data: level } = useLevelDetails(levelId)
	const {
		data: material,
		isLoading,
		refetch,
		isFetching,
	} = useLearningMaterial({ levelId: Number(id), type }, { enabled: started })

	const startHandler = () => {
		queryClient.removeQueries({
			queryKey: QUERY_KEYS.LEARNING_MATERIAL({ levelId, type }),
		})
		setStarted(true)
		refetch()
	}

	const restartHandler = () => {
		queryClient.removeQueries({
			queryKey: QUERY_KEYS.LEARNING_MATERIAL({ levelId, type }),
		})
		refetch()
	}

	const WordProgress = ({ wordId }: { wordId: number }) => {
		const { data: progress } = useWordProgress(wordId)
		return progress ? (
			<div className='word-progress'>
				<Progress
					percent={Math.round(progress.averageProgress)}
					size='small'
					showInfo={false}
					strokeColor={{
						'0%': '#FF6B35',
						'100%': '#4ECDC4',
					}}
				/>
				<Text type='secondary'>{Math.round(progress.averageProgress)}%</Text>
			</div>
		) : null
	}

	return (
		<div className='level-page'>
			<Card className='learning-card'>
				{!started ? (
					<>
						<div className='level-header'>
							<Title level={3}>
								Уровень {level?.orderNumber} {level?.title}
							</Title>
						</div>

						<Row gutter={24}>
							<Col xs={24} md={12}>
								<div className='learning-controls'>
									<Title level={4}>Выберите режим обучения</Title>
									<Select<LearningType>
										value={type}
										onChange={setType}
										style={{ width: '100%', marginBottom: 16 }}
										options={learningModes.map(mode => ({
											value: mode.value,
											label: mode.label,
										}))}
									/>
									<Button type='primary' onClick={startHandler} block>
										Начать уровень
									</Button>
								</div>
							</Col>
							<Col xs={24} md={12}>
								<div className='learning-description'>
									<Title level={4}>О режиме обучения</Title>
									<Paragraph>
										{
											learningModes.find(mode => mode.value === type)
												?.description
										}
									</Paragraph>
									<div className='learning-tips'>
										<Title level={5}>Советы:</Title>
										<ul>
											<li>Старайтесь отвечать быстро, но внимательно</li>
											<li>
												Если не уверены в ответе, пропустите и вернитесь позже
											</li>
											<li>Регулярно повторяйте пройденные слова</li>
										</ul>
									</div>
								</div>
							</Col>
						</Row>

						{progress && (
							<div className='progress-section'>
								<Text type='secondary'>Ваш прогресс по уровню</Text>
								<Progress
									percent={Math.round(progress.averageProgress)}
									status='active'
									strokeColor={{
										'0%': '#FF6B35',
										'100%': '#4ECDC4',
									}}
								/>
							</div>
						)}
					</>
				) : isLoading ? (
					<Skeleton active />
				) : (
					material && (
						<>
							<Title level={3} className='exercise-title'>
								{material.targetWord.word}
							</Title>

							<LearningExercise
								material={material}
								onNext={refetch}
								onRestart={restartHandler}
								levelId={levelId}
							/>
						</>
					)
				)}
			</Card>

			{!started && (
				<Card
					title='Слова в уровне'
					loading={wordsLoading}
					className='words-card'
				>
					<List
						dataSource={words}
						renderItem={w => (
							<List.Item>
								<div className='word-list-item'>
									<div className='word-content'>
										<strong>{w.word}</strong>&nbsp;—&nbsp;{w.definition}
									</div>
									<WordProgress wordId={w.id} />
								</div>
							</List.Item>
						)}
						style={{ maxHeight: 400, overflowY: 'auto' }}
					/>
				</Card>
			)}

			{isAuthor && !started && (
				<Button
					type='dashed'
					icon={<PlusOutlined />}
					onClick={() => setWordModal(true)}
					className='add-word-button'
				>
					Добавить слово
				</Button>
			)}

			<WordFormModal
				open={wordModal}
				levelId={Number(id)}
				onClose={() => setWordModal(false)}
			/>
		</div>
	)
}
