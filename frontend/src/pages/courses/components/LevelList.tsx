import { useQuery } from '@tanstack/react-query'
import { useDeleteLevel } from '@/hooks/api/level.hooks'
import { useWordsByLevel } from '@/hooks/api/word.hooks'
import type { LevelResponseDTO } from '@/shared/types/level'
import { DeleteOutlined } from '@ant-design/icons'
import { Button, List, message, Space, Typography, Spin } from 'antd'
import { Link } from 'react-router-dom'
import axios from 'axios'

interface Props {
	levels: LevelResponseDTO[]
	isAuthor: boolean
	isEditing: boolean
}


export const LevelList = ({ levels, isAuthor, isEditing }: Props) => {
	const deleteLevel = useDeleteLevel()

	const handleDeleteLevel = async (levelId: number) => {
		try {
			await deleteLevel.mutateAsync(levelId)
			message.success('Уровень успешно удален')
		} catch (error) {
			message.error('Ошибка при удалении уровня')
		}
	}

	return (
		<List
			dataSource={levels}
			renderItem={(level) => {
				const { data: words, isLoading } = useWordsByLevel(level.id)

				return (
					<List.Item
						actions={[
							...(!isEditing
								? [
										<Link key='enter' to={`/level/${level.id}`}>
											Перейти
										</Link>,
								  ]
								: []),
							...(isAuthor && isEditing
								? [
										<Button
											key='delete'
											type='text'
											danger
											icon={<DeleteOutlined />}
											onClick={() => handleDeleteLevel(level.id)}
										/>,
								  ]
								: []),
						]}
					>
						<List.Item.Meta
							title={
								<Space>
									<Typography.Text strong>{level.name}</Typography.Text>
									{isLoading ? (
										<Spin size='small' />
									) : (
										<Typography.Text type='secondary'>
											Слов: {words?.length || 0}
										</Typography.Text>
									)}
								</Space>
							}
						/>
					</List.Item>
				)
			}}
		/>
	)
}
