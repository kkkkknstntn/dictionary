import { authAxios } from '@/services/api'
import { progressService } from '@/services/api/progress.service'
import type { ProgressAverageDTO } from '@/shared/types/progress'
import { useQuery } from '@tanstack/react-query'

export const useWordProgress = (wordId: number) => {
	return useQuery({
		queryKey: ['progress', 'word', wordId],
		queryFn: async () => {
			const response = await authAxios.get<ProgressAverageDTO>(
				`/api/progress/word/${wordId}`
			)
			return response.data
		},
		enabled: !!wordId,
	})
}

export const useLevelProgress = (levelId: number) => {
	return useQuery({
		queryKey: ['progress', 'level', levelId],
		queryFn: async () => {
			const response = await authAxios.get<ProgressAverageDTO>(
				`/api/progress/level/${levelId}`
			)
			return response.data
		},
		enabled: !!levelId,
	})
}

export const useCourseProgress = (courseId: number | undefined) => {
	return useQuery({
		queryKey: courseId ? ['courseProgress', courseId] : [],
		queryFn: () => progressService.getCourseProgress(courseId!),
		enabled: !!courseId,
	})
}
