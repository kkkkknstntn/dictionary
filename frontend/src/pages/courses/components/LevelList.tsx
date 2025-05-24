import { useDeleteLevel } from '@/hooks/api/level.hooks'
import type { LevelResponseDTO } from '@/shared/types/level'
import { DeleteOutlined } from '@ant-design/icons'
import { Button, List, message, Space, Typography } from 'antd'
import { Link } from 'react-router-dom'

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
			renderItem={level => (
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
								<Typography.Text type='secondary'>
									Слов: {level.wordsCount}
								</Typography.Text>
							</Space>
						}
					/>
				</List.Item>
			)}
		/>
	)
}
