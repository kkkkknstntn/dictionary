import { useCreateCourse } from '@/hooks/api/course.hooks'
import type { CourseRequestDTO } from '@/shared/types/course'
import { Form, Input, Modal, notification } from 'antd'
import { useNavigate } from 'react-router-dom'

type Props = {
	open: boolean
	onClose: () => void
}

export const AddCourseModal = ({ open, onClose }: Props) => {
	const [form] = Form.useForm<CourseRequestDTO>()
	const navigate = useNavigate()
	const { mutate, isPending } = useCreateCourse()

	const handleOk = () => {
		form.validateFields().then(values => {
			mutate(values, {
				onSuccess: course => {
					notification.success({ message: 'Курс создан' })
					onClose()
					navigate(`/course/${course.id}`)
				},
			})
		})
	}

	return (
		<Modal
			title='Создать курс'
			open={open}
			onOk={handleOk}
			onCancel={onClose}
			confirmLoading={isPending}
			okText='Создать'
			cancelText='Отмена'
			destroyOnClose
		>
			<Form form={form} layout='vertical' preserve={false}>
				<Form.Item
					name='title'
					label='Название'
					rules={[{ required: true, message: 'Введите название курса' }]}
				>
					<Input />
				</Form.Item>

				<Form.Item name='description' label='Описание'>
					<Input.TextArea rows={4} />
				</Form.Item>
			</Form>
		</Modal>
	)
}
