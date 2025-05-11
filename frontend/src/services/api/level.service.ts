import type { LevelRequestDTO, LevelResponseDTO } from '@/shared/types/level'
import { authAxios } from './index'

export const levelService = {
	getAllLevels: async (courseId?: number): Promise<LevelResponseDTO[]> => {
		const response = await authAxios.get<LevelResponseDTO[]>(
			'/api/levels',
			courseId ? { params: { courseId } } : undefined
		)
		return response.data
	},

	getLevelById: async (id: number): Promise<LevelResponseDTO> => {
		const response = await authAxios.get<LevelResponseDTO>(`/api/levels/${id}`)
		return response.data
	},

	createLevel: async (data: LevelRequestDTO): Promise<LevelResponseDTO> => {
		const response = await authAxios.post<LevelResponseDTO>('/api/levels', data)
		return response.data
	},

	updateLevel: async (
		id: number,
		data: LevelRequestDTO
	): Promise<LevelResponseDTO> => {
		const response = await authAxios.put<LevelResponseDTO>(
			`/api/levels/${id}`,
			data
		)
		return response.data
	},

	deleteLevel: async (id: number): Promise<void> => {
		await authAxios.delete(`/api/levels/${id}`)
	},
}
