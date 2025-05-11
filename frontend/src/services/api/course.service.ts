import type {
	CourseRequestDTO,
	CourseResponseDTO,
	CourseSearchParams,
} from '@/shared/types/course'
import { authAxios } from './index'

export const courseService = {
	getAllCourses: async (): Promise<CourseResponseDTO[]> => {
		const response = await authAxios.get<CourseResponseDTO[]>('/api/courses')
		return response.data
	},

	createCourse: async (data: CourseRequestDTO): Promise<CourseResponseDTO> => {
		const response = await authAxios.post<CourseResponseDTO>(
			'/api/courses',
			data
		)
		return response.data
	},

	updateCourse: async (
		id: number,
		data: CourseRequestDTO
	): Promise<CourseResponseDTO> => {
		const response = await authAxios.put<CourseResponseDTO>(
			`/api/courses/${id}`,
			data
		)
		return response.data
	},

	deleteCourse: async (id: number): Promise<void> => {
		await authAxios.delete(`/api/courses/${id}`)
	},

	joinCourse: async (id: number): Promise<void> => {
		await authAxios.post(`/api/courses/${id}/join`)
	},

	getUserCourses: async (): Promise<CourseResponseDTO[]> => {
		const response = await authAxios.get<CourseResponseDTO[]>(
			'/api/courses/my-courses'
		)
		return response.data
	},

	searchCourses: async (
		params: CourseSearchParams
	): Promise<CourseResponseDTO[]> => {
		const response = await authAxios.get<CourseResponseDTO[]>(
			'/api/courses/search',
			{ params }
		)
		return response.data
	},
}
