import type { CourseResponseDTO } from './course'

export type UserUpdateData = {
	username?: string
	password?: string
}

export type UserResponseDTO = {
	isEnabled: any
	id: number
	username: string
	roles: string[]
	createdAt: string
	courses?: CourseResponseDTO[]
	imagePath?: string
}
