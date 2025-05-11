import { authService } from '@/services/api/auth.service'
import type { LoginFormData, RegisterFormData } from '@/shared/types/auth'
import { useMutation, useQueryClient } from '@tanstack/react-query'

export const useLogin = () => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: (data: LoginFormData) => authService.login(data),
		onSuccess: ({ accessToken, user }) => {
			localStorage.setItem('accessToken', accessToken)
			queryClient.setQueryData(['currentUser'], user)
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
			queryClient.removeQueries()
		},
	})
}
