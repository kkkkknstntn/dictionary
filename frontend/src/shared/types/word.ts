export interface WordDTO {
	id: number
	word: string
	definition: string
	imagePath?: string
	audioPath?: string
	videoPath?: string
	levelId: number
	courseId: number
}

export interface LevelDTO {
	id: number
	title: string
	orderNumber: number
	courseId: number
}

export interface WordRequestDTO {
	word: string
	definition: string
	imagePath?: string
	audioPath?: string
	videoPath?: string
	levelId: number
}

export type WordResponseDTO = {
	id: number
	word: string
	definition: string
	imagePath?: string
	audioPath?: string
	videoPath?: string
	activeForTesting: boolean
	levelId: number
	courseId: number
}

export type WordMediaType = 'audio' | 'video' | 'image'
