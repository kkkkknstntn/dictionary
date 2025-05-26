import { useCreateLevel } from '@/hooks/api/level.hooks'
import type { LevelRequestDTO } from '@/shared/types/level'
import { Form, Input, InputNumber, Modal, notification } from 'antd'

type Props = {
	open: boolean
	courseId: number
	onClose: () => void
	onSuccess: () => void // <<< добавили сюда
}

export const AddLevelModal = ({ open, courseId, onClose, onSuccess }: Props) => {
	const [form] = Form.useForm<Omit<LevelRequestDTO, 'courseId'>>()
	const { mutate, isPending } = useCreateLevel()

	const handleOk = () => {
		form.validateFields().then(values => {
			mutate(
				{ ...values, courseId },
				{
					onSuccess: () => {
						notification.success({ message: 'Уровень создан' })
						onSuccess() // <<< вызываем родительский перезапрос
						onClose()
						form.resetFields()
					},
				}
			)
		})
	}

	return (
		<Modal
			title='Добавить уровень'
			open={open}
			onOk={handleOk}
			onCancel={onClose}
			confirmLoading={isPending}
			okText='Создать'
			cancelText='Отмена'
			destroyOnClose
		>
			<Form form={form} layout='vertical'>
				<Form.Item
					name='name'
					label='Название уровня'
					rules={[{ required: true, message: 'Введите название уровня' }]}
				>
					<Input />
				</Form.Item>

				<Form.Item
					name='orderNumber'
					label='Порядковый номер'
					tooltip='Оставьте пустым — проставится автоматически'
				>
					<InputNumber min={1} style={{ width: '100%' }} />
				</Form.Item>
			</Form>
		</Modal>
	)
}
