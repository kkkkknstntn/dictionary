import type { CourseResponseDTO } from './course'
import type { WordResponseDTO } from './word'

export type LevelRequestDTO = {
	name: string
	orderNumber?: number
	courseId: number
}

export type LevelResponseDTO = {
	id: number
	name: string
	orderNumber: number
	courseId: number
	words: WordResponseDTO[]
	course?: CourseResponseDTO
}
