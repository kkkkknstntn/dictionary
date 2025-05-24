import type { LevelRequestDTO, LevelResponseDTO } from '@/shared/types/level'
import { authAxios } from './index'

export const levelApi = {
	getLevels: (courseId?: number) =>
		authAxios.get<LevelResponseDTO[]>('/api/levels', {
			params: { courseId },
		}),

	getLevelDetails: (id: number) =>
		authAxios.get<LevelResponseDTO>(`/api/levels/${id}`),

	createLevel: (data: LevelRequestDTO) =>
		authAxios.post<LevelResponseDTO>('/api/levels', data),

	updateLevel: (id: number, data: { name: string; courseId: number }) =>
		authAxios.put<LevelResponseDTO>(`/api/levels/${id}`, data),

	deleteLevel: (id: number) => authAxios.delete(`/api/levels/${id}`),
}
