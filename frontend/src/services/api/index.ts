import axios from 'axios'
import { authService } from './auth.service'

export const authAxios = axios.create({
	withCredentials: true,
})

authAxios.interceptors.request.use(config => {
	const accessToken = localStorage.getItem('accessToken')
	const refreshToken = localStorage.getItem('refreshToken')
	if (config.url?.includes('/api/refresh')) {
		if (refreshToken) {
			config.headers.Authorization = `Bearer ${refreshToken}`
		}
	} else if (accessToken) {
		config.headers.Authorization = `Bearer ${accessToken}`
	}
	if (config.url === '/api/users/me' && !localStorage.getItem('accessToken')) {
		return Promise.reject(new Error('No access token'))
	}
	return config
})

authAxios.interceptors.response.use(
	response => response,
	async error => {
		const originalRequest = error.config
		if (error.response?.status === 403 && !originalRequest._retry) {
			originalRequest._retry = true
			try {
				const { accessToken } = await authService.refresh()
				localStorage.setItem('accessToken', accessToken)
				return authAxios(originalRequest)
			} catch (refreshError) {
				localStorage.removeItem('accessToken')
				localStorage.removeItem('refreshToken')
				window.location.href = '/login'
				return Promise.reject(refreshError)
			}
		}
		return Promise.reject(error)
	}
)
