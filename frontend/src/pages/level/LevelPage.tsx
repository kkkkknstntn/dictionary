import { queryClient } from '@/app/QueryProvider'
import { useLearningMaterial } from '@/hooks/api/learn.hooks'
import { useIsCourseAuthor } from '@/hooks/api/level-extra.hooks'
import { useCurrentUser } from '@/hooks/api/user.hooks'
import { useWordsByLevel } from '@/hooks/api/word.hooks'
import { QUERY_KEYS } from '@/shared/constants/queryKeys'
import type { LearningType } from '@/shared/types/learn'
import { PlusOutlined, ReloadOutlined } from '@ant-design/icons'
import { Button, Card, List, Select, Skeleton, Typography } from 'antd'
import { useState } from 'react'
import { useParams } from 'react-router-dom'
import { LearningExercise } from './components/LearningExercise'
import { WordFormModal } from './components/WordFormModal'
export const LevelPage = () => {
	const { id } = useParams<{ id: string }>()
	const levelId = Number(id)
	const { data: currentUser } = useCurrentUser()
	const [wordModal, setWordModal] = useState(false)
	const { Title } = Typography
	const { data: isAuthor } = useIsCourseAuthor(levelId, currentUser?.username)
	const [started, setStarted] = useState(false)
	const [type, setType] = useState<LearningType>('WORD_TO_IMAGE')
	const { data: words, isLoading: wordsLoading } = useWordsByLevel(levelId)
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
	return (
		<div className='level-page'>
			<Card className='learning-card'>
				{!started && (
					<div style={{ textAlign: 'center' }}>
						<Select<LearningType>
							value={type}
							onChange={setType}
							style={{ width: 240, marginBottom: 16 }}
							options={[
								{ value: 'WORD_TO_IMAGE', label: 'Слово → картинка' },
								{ value: 'IMAGE_TO_WORD', label: 'Картинка → слово' },
								{ value: 'AUDIO_TO_WORD', label: 'Аудио → слово' },
							]}
						/>
						<br />
						<Button type='primary' onClick={startHandler}>
							Начать уровень
						</Button>
					</div>
				)}

				{started && isLoading ? (
					<Skeleton active />
				) : (
					started &&
					material && (
						<>
							<Title level={3} className='exercise-title'>
								{material.targetWord.word}
							</Title>

							<LearningExercise material={material} onNext={refetch} />

							<Button
								icon={<ReloadOutlined />}
								style={{ marginTop: 24 }}
								disabled={isFetching}
								onClick={restartHandler}
							>
								Перезапустить уровень
							</Button>
						</>
					)
				)}
			</Card>
			{!started && (
				<Card
					title='Слова в уровне'
					loading={wordsLoading}
					style={{ marginTop: 24 }}
					bodyStyle={{ padding: 20 }}
				>
					<List
						dataSource={words}
						renderItem={w => (
							<List.Item>
								<strong>{w.word}</strong>&nbsp;—&nbsp;{w.definition}
							</List.Item>
						)}
						style={{ maxHeight: 400, overflowY: 'auto' }}
					/>
				</Card>
			)}
			{/* Кнопка «добавить слово» доступна автору курса */}
			{isAuthor && (
				<Button
					type='dashed'
					icon={<PlusOutlined />}
					onClick={() => setWordModal(true)}
					style={{ marginTop: 24 }}
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
