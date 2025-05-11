import { useUpdateUser } from '@/hooks/api/user.hooks'
import type { UserResponseDTO, UserUpdateData } from '@/shared/types/user'
import { Button, Form, Input, notification } from 'antd'

type Props = {
	user?: UserResponseDTO
}

export const ProfileForm = ({ user }: Props) => {
	const [form] = Form.useForm()
	const { mutate, isPending } = useUpdateUser(user?.id || 0)

	const handleSubmit = (values: UserUpdateData) => {
		mutate(values, {
			onSuccess: () => {
				notification.success({
					message: 'Данные успешно обновлены',
				})
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

			<Button type='primary' htmlType='submit' loading={isPending}>
				Сохранить изменения
			</Button>
		</Form>
	)
}
