import type {
	AnswerResultDTO,
	AnswerSubmissionDTO,
	LearningMaterialDTO,
	LearningParams,
} from '@/shared/types/learn'
import { authAxios } from './index'

export const learnService = {
	getLearningMaterial: async (
		params: LearningParams
	): Promise<LearningMaterialDTO> => {
		const response = await authAxios.get<LearningMaterialDTO>(
			`/api/learn/${params.levelId}`,
			{ params: { type: params.type } }
		)
		return response.data
	},

	checkAnswer: async (data: AnswerSubmissionDTO): Promise<AnswerResultDTO> => {
		const response = await authAxios.post<AnswerResultDTO>(
			'/api/learn/check-answer',
			data
		)
		return response.data
	},
}
