import { useUpdateUser } from '@/hooks/api/user.hooks'
import type { UserResponseDTO, UserUpdateData } from '@/shared/types/user'
import { UploadOutlined } from '@ant-design/icons'
import { Button, Form, Input, notification, Upload } from 'antd'
import type { UploadFile } from 'antd/es/upload/interface'
import { useState } from 'react'

type Props = {
	user?: UserResponseDTO
}

export const ProfileForm = ({ user }: Props) => {
	const [form] = Form.useForm()
	const { mutate, isPending } = useUpdateUser(user?.id || 0)
	const [fileList, setFileList] = useState<UploadFile[]>([])

	const handleSubmit = (values: UserUpdateData) => {
		const formData = new FormData()
		formData.append('username', values.username)
		if (values.password) {
			formData.append('password', values.password)
		}
		if (fileList[0]?.originFileObj) {
			formData.append('imageFile', fileList[0].originFileObj)
		}

		mutate(formData, {
			onSuccess: () => {
				notification.success({
					message: 'Данные успешно обновлены',
				})
				setFileList([])
			},
			onError: () => {
				notification.error({
					message: 'Ошибка при обновлении данных',
				})
			},
		})
	}

	return (
		<Form
			form={form}
			layout='vertical'
			initialValues={user}
			onFinish={handleSubmit}
			className='profile-form'
		>
			<Form.Item
				label='Имя пользователя'
				name='username'
				rules={[{ required: true, message: 'Введите имя пользователя' }]}
			>
				<Input />
			</Form.Item>

			<Form.Item
				label='Новый пароль'
				name='password'
				rules={[{ min: 8, message: 'Минимум 8 символов' }]}
			>
				<Input.Password placeholder='Оставьте пустым, если не меняется' />
			</Form.Item>

			<Form.Item label='Фото профиля'>
				<Upload
					listType='picture'
					maxCount={1}
					fileList={fileList}
					onChange={({ fileList }) => setFileList(fileList)}
					beforeUpload={() => false}
				>
					<Button icon={<UploadOutlined />}>Загрузить фото</Button>
				</Upload>
			</Form.Item>

			<Button type='primary' htmlType='submit' loading={isPending}>
				Сохранить изменения
			</Button>
		</Form>
	)
}
