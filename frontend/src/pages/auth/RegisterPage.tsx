import { PasswordStrength } from '@/components/auth/PasswordStrength'
import { EmailServiceModal } from '@/components/modals/EmailServiceModal'
import { useRegister } from '@/hooks/api/auth.hooks'
import type { RegisterFormData } from '@/shared/types/auth'
import { Button, Card, Form, Input, Modal } from 'antd'
import { useState } from 'react'
import { Link } from 'react-router-dom'
import './AuthPages.scss'

export const RegisterPage = () => {
	const [form] = Form.useForm()
	const { mutate, isPending } = useRegister()
	const [successModalOpen, setSuccessModalOpen] = useState(false)
	const [registeredEmail, setRegisteredEmail] = useState('')
	const [password, setPassword] = useState('')

	const handleSubmit = (values: RegisterFormData) => {
		mutate(values, {
			onSuccess: () => {
				setRegisteredEmail(values.username)
				setSuccessModalOpen(true)
				form.resetFields()
			},
			onError: error => {
				Modal.error({
					title: 'Ошибка регистрации',
					content: error.message || 'Произошла ошибка при регистрации',
				})
			},
		})
	}

	return (
		<div className='auth-page'>
			<Card title='Регистрация' className='auth-card'>
				<div className='auth-info'>
					Создайте аккаунт, чтобы начать изучать новые слова и отслеживать свой
					прогресс
				</div>
				<Form form={form} onFinish={handleSubmit}>
					<Form.Item
						name='username'
						rules={[
							{ required: true, message: 'Введите email' },
							{ type: 'email', message: 'Введите корректный email' },
						]}
					>
						<Input placeholder='Email' />
					</Form.Item>

					<Form.Item
						name='password'
						rules={[
							{
								required: true,
								message: 'Введите пароль',
							},
							{
								min: 8,
								message: 'Пароль должен быть не менее 8 символов',
							},
						]}
					>
						<Input.Password
							placeholder='Пароль'
							onChange={e => setPassword(e.target.value)}
						/>
					</Form.Item>

					<Form.Item
						name='confirmPassword'
						dependencies={['password']}
						rules={[
							{
								required: true,
								message: 'Подтвердите пароль',
							},
							({ getFieldValue }) => ({
								validator(_, value) {
									if (!value || getFieldValue('password') === value) {
										return Promise.resolve()
									}
									return Promise.reject(new Error('Пароли не совпадают'))
								},
							}),
						]}
					>
						<Input.Password placeholder='Подтвердите пароль' />
					</Form.Item>

					<PasswordStrength password={password} />

					<Button type='primary' htmlType='submit' loading={isPending} block>
						Зарегистрироваться
					</Button>

					<div className='auth-link'>
						Уже есть аккаунт? <Link to='/login'>Войти</Link>
					</div>
				</Form>
			</Card>

			<EmailServiceModal
				email={registeredEmail}
				open={successModalOpen}
				onClose={() => setSuccessModalOpen(false)}
			/>
		</div>
	)
}
