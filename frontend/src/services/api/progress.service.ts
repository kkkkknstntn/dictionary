import { ProgressAverageDTO } from '@/shared/types/progress'
import { authAxios } from './index'

export const progressService = {
	getCourseProgress: async (courseId: number): Promise<ProgressAverageDTO> => {
		const response = await authAxios.get<ProgressAverageDTO>(
			`/api/progress/course/${courseId}`
		)
		return response.data
	},

	getWordProgress: (wordId: number) =>
		authAxios
			.get<ProgressAverageDTO>(`/api/progress/word/${wordId}`)
			.then(res => res.data),

	getLevelProgress: (levelId: number) =>
		authAxios
			.get<ProgressAverageDTO>(`/api/progress/level/${levelId}`)
			.then(res => res.data),
}
