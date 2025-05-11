import { useRegister } from '@/hooks/api/auth.hooks'
import type { RegisterFormData } from '@/shared/types/auth'
import { Button, Card, Form, Input, notification } from 'antd'
import { Link } from 'react-router-dom'
import './AuthPages.scss'

export const RegisterPage = () => {
	const [form] = Form.useForm()
	const { mutate, isPending } = useRegister()

	const handleSubmit = (values: RegisterFormData) => {
		mutate(values, {
			onSuccess: () => {
				notification.success({
					message: 'Успешно',
					description: 'Аккаунт создан. Проверьте почту для активации',
				})
				form.resetFields()
			},
			onError: error => {
				notification.error({
					message: 'Ошибка',
					description: error.message || 'Ошибка регистрации',
				})
			},
		})
	}

	return (
		<div className='auth-page'>
			<Card title='Регистрация' className='auth-card'>
				<Form form={form} onFinish={handleSubmit}>
					<Form.Item
						name='username'
						rules={[{ required: true, message: 'Введите имя пользователя' }]}
					>
						<Input placeholder='Имя пользователя' />
					</Form.Item>

					<Form.Item
						name='password'
						rules={[
							{
								required: true,
								min: 8,
								message: 'Пароль должен быть не менее 8 символов',
							},
						]}
					>
						<Input.Password placeholder='Пароль' />
					</Form.Item>

					<Button type='primary' htmlType='submit' loading={isPending} block>
						Зарегистрироваться
					</Button>

					<div className='auth-link'>
						Уже есть аккаунт? <Link to='/login'>Войти</Link>
					</div>
				</Form>
			</Card>
		</div>
	)
}
