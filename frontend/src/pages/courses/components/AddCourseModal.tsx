import { useCreateCourse } from '@/hooks/api/course.hooks'
import type { CourseRequestDTO } from '@/shared/types/course'
import { UploadOutlined } from '@ant-design/icons' // нужен для кнопки Upload
import { Button, Form, Input, Modal, notification, Upload } from 'antd'
import type { UploadFile } from 'antd/es/upload' // тип для файла
import { useNavigate } from 'react-router-dom'

type Props = {
	open: boolean
	onClose: () => void
}
type CourseFormValues = {
	title: string
	description?: string
	imageFile?: UploadFile[]
}
export const AddCourseModal = ({ open, onClose }: Props) => {
	const [form] = Form.useForm<CourseFormValues>()
	const navigate = useNavigate()
	const { mutate, isPending } = useCreateCourse()

	const handleOk = () => {
		form.validateFields().then(values => {
			const file =
				Array.isArray(values.imageFile) && values.imageFile.length > 0
					? values.imageFile[0].originFileObj
					: undefined

			const payload: CourseRequestDTO = {
				title: values.title,
				description: values.description,
				imageFile: file,
			}

			mutate(payload, {
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

				{/* 👇 новое поле для изображения */}
				<Form.Item
					name='imageFile'
					label='Изображение'
					valuePropName='fileList'
					getValueFromEvent={e => (Array.isArray(e) ? e : e?.fileList)}
				>
					<Upload
						beforeUpload={() => false} // не загружаем автоматически
						maxCount={1}
						accept='image/*'
					>
						<Button icon={<UploadOutlined />}>Загрузить</Button>
					</Upload>
				</Form.Item>
			</Form>
		</Modal>
	)
}
