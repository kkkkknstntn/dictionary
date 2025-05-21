import { useWordsByLevel } from '@/hooks/api/word.hooks'
import { Card, List, Space, Tag, Typography } from 'antd'
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
			grid={{ gutter: 16, xs: 1, sm: 2, md: 3, lg: 3, xl: 4, xxl: 4 }}
			dataSource={words}
			loading={isLoading}
			renderItem={word => (
				<List.Item>
					<Link to={`/word/${word.id}`}>
						<Card hoverable className='word-card'>
							<Space
								direction='vertical'
								size='small'
								style={{ width: '100%' }}
							>
								<Title level={4}>{word.word}</Title>
								<Text type='secondary' ellipsis={{ rows: 2 }}>
									{word.definition}
								</Text>
								{word.imagePath && (
									<img
										src={word.imagePath}
										alt={word.word}
										className='word-image'
									/>
								)}
								<Space>
									{word.audioPath && <Tag color='blue'>Аудио</Tag>}
									{word.videoPath && <Tag color='green'>Видео</Tag>}
									{word.imagePath && <Tag color='purple'>Изображение</Tag>}
								</Space>
							</Space>
						</Card>
					</Link>
				</List.Item>
			)}
		/>
	)
}
