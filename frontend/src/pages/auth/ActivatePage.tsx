import { authService } from '@/services/api/auth.service'
import { Button, Result, Spin } from 'antd'
import { useEffect } from 'react'
import { useSearchParams } from 'react-router-dom'

export const ActivatePage = () => {
	const [searchParams] = useSearchParams()
	const token = searchParams.get('token')
	const email = searchParams.get('email')

	useEffect(() => {
		if (token && email) {
			authService.activateAccount(token, email).catch(() => {})
		}
	}, [token, email])

	if (!token || !email) {
		return (
			<Result
				status='error'
				title='Invalid activation link'
				extra={<Button href='/login'>Go to Login</Button>}
			/>
		)
	}

	return (
		<div className='flex-center h-screen'>
			<Spin size='large' tip='Activating account...' />
		</div>
	)
}
