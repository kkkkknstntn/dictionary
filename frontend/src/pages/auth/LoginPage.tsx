import { useLogin } from '@/hooks/api/auth.hooks'
import type { LoginFormData } from '@/shared/types/auth'
import { Button, Card, Form, Input } from 'antd'
import axios from 'axios'
import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import './AuthPages.scss'

export const LoginPage = () => {
	const [form] = Form.useForm()
	const { mutate, isPending } = useLogin()
	const navigate = useNavigate()
	const [loginError, setLoginError] = useState<string>('')

	const getErrorMessage = (error: unknown) => {
		if (axios.isAxiosError(error)) {
			const status = error.response?.status
			const message = error.response?.data?.message

			switch (status) {
				case 401:
					return 'Неверный email или пароль'
				case 403:
					return 'Аккаунт не активирован. Пожалуйста, проверьте вашу почту'
				case 404:
					return 'Аккаунт не найден'
				default:
					return message || 'Произошла ошибка при входе. Попробуйте позже'
			}
		}
		return 'Произошла неизвестная ошибка'
	}

	const handleSubmit = (values: LoginFormData) => {
		setLoginError('')
		mutate(values, {
			onSuccess: () => {
				navigate('/courses')
			},
			onError: (error: unknown) => {
				const errorMessage = getErrorMessage(error)
				setLoginError(errorMessage)
			},
		})
	}

	return (
		<div className='auth-page'>
			<Card title='Вход в систему' className='auth-card'>
				<div className='auth-info'>
					Добро пожаловать! Войдите в свой аккаунт, чтобы продолжить обучение
				</div>
				<Form form={form} onFinish={handleSubmit}>
					{loginError && (
						<Form.Item>
							<div style={{ color: '#ff4d4f', marginBottom: '16px' }}>
								{loginError}
							</div>
						</Form.Item>
					)}
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
						rules={[{ required: true, message: 'Введите пароль' }]}
					>
						<Input.Password placeholder='Пароль' />
					</Form.Item>

					<Button type='primary' htmlType='submit' loading={isPending} block>
						Войти
					</Button>

					<div className='auth-divider'>или</div>

					<div className='auth-link'>
						Нет аккаунта? <Link to='/register'>Зарегистрироваться</Link>
					</div>
				</Form>
			</Card>
		</div>
	)
}
