import { authService } from '@/services/api/auth.service'
import type { LoginFormData, RegisterFormData } from '@/shared/types/auth'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import axios from 'axios'

export const useActivateAccount = (token: string, email: string) => {
	return useQuery({
		queryKey: ['activate', token, email],
		queryFn: () => authService.activateAccount(token, email),
		retry: false,
		enabled: !!token && !!email,
	})
}

export const useLogin = () => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: async (data: LoginFormData) => {
			console.log('Login mutation started with data:', data)
			try {
				const response = await authService.login(data)
				console.log('Login response:', response)
				return response
			} catch (error) {
				console.log('Login error in mutationFn:', error)
				throw error
			}
		},
		onSuccess: ({ accessToken, refreshToken, user }) => {
			console.log('Login success, setting tokens and user data')
			localStorage.setItem('accessToken', accessToken)
			localStorage.setItem('refreshToken', refreshToken)
			queryClient.setQueryData(['currentUser'], user)
		},
		onError: error => {
			console.log('Login error in onError:', error)
			if (axios.isAxiosError(error)) {
				throw error
			}
			throw new Error('Произошла неизвестная ошибка')
		},
	})
}

export const useRegister = () => {
	return useMutation({
		mutationFn: (data: RegisterFormData) => authService.register(data),
	})
}

export const useLogout = () => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: () => authService.logout(),
		onSuccess: () => {
			localStorage.removeItem('accessToken')
			localStorage.removeItem('refreshToken')
			queryClient.removeQueries()
		},
	})
}
