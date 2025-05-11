import type {
	AuthResponse,
	LoginFormData,
	RegisterFormData,
} from '@/shared/types/auth'
import type { UserResponseDTO } from '@/shared/types/user'
import { authAxios } from './index'

export const authService = {
	async login(data: LoginFormData) {
		const response = await authAxios.post<AuthResponse>('/api/login', data)
		return response.data
	},

	async register(data: RegisterFormData) {
		const response = await authAxios.post<UserResponseDTO>('/api/users', data)
		return response.data
	},

	async logout() {
		await authAxios.post('/api/logout')
	},

	async refresh() {
		const response = await authAxios.post<AuthResponse>('/api/refresh')
		return response.data
	},

	async activateAccount(token: string, email: string) {
		await authAxios.get('/api/activation/activate', {
			params: { token, email },
		})
	},
}
