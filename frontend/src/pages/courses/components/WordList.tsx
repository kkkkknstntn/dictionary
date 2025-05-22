import { useWordsByLevel } from '@/hooks/api/word.hooks'
import { Card, List, Tag, Typography } from 'antd'
import { Link } from 'react-router-dom'
import './WordList.scss'

const { Title, Text } = Typography

interface Props {
	levelId: number
}

export const WordList = ({ levelId }: Props) => {
	const { data: words, isLoading } = useWordsByLevel(levelId)

	return (
		<List
			grid={{ gutter: 24, xs: 1, sm: 2, md: 2, lg: 3, xl: 3, xxl: 4 }}
			dataSource={words}
			loading={isLoading}
			renderItem={word => (
				<List.Item>
					<Link to={`/word/${word.id}`}>
						<Card hoverable className='word-card'>
							<div className='word-content'>
								<Title level={4} className='word-title'>
									{word.word}
								</Title>
								<Text type='secondary' className='word-definition'>
									{word.definition}
								</Text>
								{word.imagePath && (
									<img
										src={word.imagePath}
										alt={word.word}
										className='word-image'
									/>
								)}
								<div className='word-tags'>
									<div className='tags-row'>
										{word.audioPath && <Tag color='blue'>Аудио</Tag>}
										{word.videoPath && <Tag color='green'>Видео</Tag>}
									</div>
									<div className='tags-row'>
										{word.imagePath && <Tag color='purple'>Изображение</Tag>}
									</div>
								</div>
							</div>
						</Card>
					</Link>
				</List.Item>
			)}
		/>
	)
}
