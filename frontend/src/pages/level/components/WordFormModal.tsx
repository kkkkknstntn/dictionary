import { useCreateWord } from '@/hooks/api/word.hooks'
import { useEffect } from 'react'
import type { WordRequestDTO } from '@/shared/types/word'
import { UploadOutlined } from '@ant-design/icons'
import { Button, Form, Input, Modal, Switch, Upload, notification } from 'antd'
import type { UploadChangeParam, UploadFile } from 'antd/es/upload/interface'
type Props = {
	open: boolean
	levelId: number
	onClose: () => void
}

export const WordFormModal = ({ open, onClose, levelId }: Props) => {
	const [form] = Form.useForm<Omit<WordRequestDTO, 'levelId'>>()
	const { mutate, isPending } = useCreateWord()

	useEffect(() => {
		if (open) {
			form.resetFields()
		}
	}, [open, form])

	const handleOk = () => {
		form.validateFields().then(values => {
			mutate(
				{ ...values, levelId },
				{
					onSuccess: () => {
						notification.success({ message: 'Слово добавлено' })
						onClose()
					},
				}
			)
		})
	}

	/* Upload пропускаем файл как is (antd сохраняет File в value) */
	const normFile = (
		e: UploadChangeParam<UploadFile<File>>
	): File | undefined => {
		if (Array.isArray(e)) return undefined
		return e?.fileList?.[0]?.originFileObj as File | undefined
	}
	return (
		<Modal
			open={open}
			title='Добавить слово'
			onOk={handleOk}
			onCancel={onClose}
			confirmLoading={isPending}
			okText='Сохранить'
			cancelText='Отмена'
			destroyOnClose
		>
			<Form form={form} layout='vertical'>
				<Form.Item
					label='Слово'
					name='word'
					rules={[{ required: true, message: 'Введите слово' }]}
				>
					<Input />
				</Form.Item>

				<Form.Item
					label='Определение'
					name='definition'
					rules={[{ required: true, message: 'Введите определение' }]}
				>
					<Input.TextArea rows={3} />
				</Form.Item>

				<Form.Item
					label='Участвует в тестах'
					name='activeForTesting'
					valuePropName='checked'
					initialValue={true}
				>
					<Switch />
				</Form.Item>

				<Form.Item
					label='Изображение'
					name='imageFile'
					valuePropName='file'
					getValueFromEvent={normFile}
				>
					<Upload beforeUpload={() => false} maxCount={1}>
						<Button icon={<UploadOutlined />}>Загрузить</Button>
					</Upload>
				</Form.Item>

				<Form.Item
					label='Аудио'
					name='audioFile'
					valuePropName='file'
					getValueFromEvent={normFile}
				>
					<Upload beforeUpload={() => false} maxCount={1}>
						<Button icon={<UploadOutlined />}>Загрузить</Button>
					</Upload>
				</Form.Item>

				<Form.Item
					label='Видео'
					name='videoFile'
					valuePropName='file'
					getValueFromEvent={normFile}
				>
					<Upload beforeUpload={() => false} maxCount={1}>
						<Button icon={<UploadOutlined />}>Загрузить</Button>
					</Upload>
				</Form.Item>
			</Form>
		</Modal>
	)
}
