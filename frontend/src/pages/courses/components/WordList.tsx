import { useWordsByLevel } from '@/hooks/api/word.hooks'
import { Card, List, Typography } from 'antd'
import { Link } from 'react-router-dom'
import './WordList.scss'

const { Title } = Typography

interface Props {
	levels: Array<{ id: number; title: string; courseId: number }>
}

export const WordList = ({ levels }: Props) => {
	return (
		<div className='word-list'>
			{levels.map(level => (
				<div key={level.id} className='level-section'>
					<Title level={4} className='level-title'>
						{level.title}
					</Title>
					<LevelWords levelId={level.id} courseId={level.courseId} />
				</div>
			))}
		</div>
	)
}

const LevelWords = ({
	levelId,
	courseId,
}: {
	levelId: number
	courseId: number
}) => {
	const { data: words } = useWordsByLevel(levelId)

	if (!words?.length) return null

	return (
		<List
			grid={{ gutter: 16, xs: 1, sm: 2, md: 3, lg: 3, xl: 4, xxl: 4 }}
			dataSource={words}
			renderItem={word => (
				<List.Item>
					<Link to={`/course/${courseId}/word/${word.id}`}>
						<Card hoverable className='word-card'>
							<Title level={5}>{word.word}</Title>
							<p className='definition'>{word.definition}</p>
						</Card>
					</Link>
				</List.Item>
			)}
		/>
	)
}
