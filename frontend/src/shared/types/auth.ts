import type { UserResponseDTO } from './user'

export type LoginFormData = {
	username: string
	password: string
}

export type RegisterFormData = {
	username: string
	password: string
}

export type Tokens = {
	accessToken: string
	refreshToken: string
}

export type AuthResponse = {
	user: UserResponseDTO
} & Tokens
