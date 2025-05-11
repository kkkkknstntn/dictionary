import { learnService } from '@/services/api/learn.service'
import { QUERY_KEYS } from '@/shared/constants/queryKeys'
import type { AnswerSubmissionDTO, LearningParams } from '@/shared/types/learn'
import { useMutation, useQuery } from '@tanstack/react-query'

export const useLearningMaterial = (params: LearningParams) => {
	return useQuery({
		queryKey: QUERY_KEYS.LEARNING_MATERIAL(params),
		queryFn: () => learnService.getLearningMaterial(params),
		enabled: !!params.levelId && !!params.type,
	})
}

export const useCheckAnswer = () => {
	return useMutation({
		mutationFn: (data: AnswerSubmissionDTO) => learnService.checkAnswer(data),
	})
}
