export type WordRequestDTO = {
	word: string
	definition: string
	levelId: number
	activeForTesting?: boolean
	audioFile?: File | null
	videoFile?: File | null
	imageFile?: File | null
}

export type WordResponseDTO = {
	id: number
	word: string
	definition: string
	imagePath?: string
	audioPath?: string
	videoPath?: string
	activeForTesting: boolean
	level: {
		id: number
		name: string
		courseId: number
	}
}

export type WordMediaType = 'audio' | 'video' | 'image'
