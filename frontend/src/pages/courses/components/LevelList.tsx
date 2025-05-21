import type { LevelResponseDTO } from '@/shared/types/level'
import { List } from 'antd'
import { Link } from 'react-router-dom'

type Props = {
	levels: LevelResponseDTO[]
}

export const LevelList = ({ levels }: Props) => (
	<List
		itemLayout='horizontal'
		dataSource={levels}
		renderItem={level => (
			<List.Item
				actions={[
					<Link key='enter' to={`/level/${level.id}`}>
						Перейти
					</Link>,
				]}
			>
				<List.Item.Meta
					title={level.name}
					description={`Порядковый номер: ${level.orderNumber}`}
				/>
			</List.Item>
		)}
	/>
)
