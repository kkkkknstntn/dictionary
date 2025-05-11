import type { WordResponseDTO } from './word'

export type AnswerSubmissionDTO = {
	wordId: number
	answer: string
	type: LearningType
}

export type AnswerResultDTO = {
	newProgress: number
	correct: boolean
}

export type LearningMaterialDTO = {
	targetWord: WordResponseDTO
	options: string[]
	type: LearningType
}

export type LearningType = 'WORD_TO_IMAGE' | 'IMAGE_TO_WORD' | 'AUDIO_TO_WORD'

export type LearningParams = {
	levelId: number
	type: LearningType
}
