import { learnService } from '@/services/api/learn.service'
import { QUERY_KEYS } from '@/shared/constants/queryKeys'
import type {
	AnswerSubmissionDTO,
	LearningMaterialDTO,
	LearningParams,
} from '@/shared/types/learn'
import {
	useMutation,
	useQuery,
	type UseQueryOptions,
} from '@tanstack/react-query'

export const useLearningMaterial = (
	params: LearningParams,
	options?: Omit<UseQueryOptions<LearningMaterialDTO>, 'queryKey' | 'queryFn'>
) => {
	return useQuery({
		queryKey: QUERY_KEYS.LEARNING_MATERIAL(params),
		queryFn: () => learnService.getLearningMaterial(params),
		enabled: false, // выключаем автозапуск
		...options,
	})
}

export const useCheckAnswer = () => {
	return useMutation({
		mutationFn: (data: AnswerSubmissionDTO) => learnService.checkAnswer(data),
	})
}
