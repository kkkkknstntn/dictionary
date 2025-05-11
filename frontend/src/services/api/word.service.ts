import type { WordRequestDTO, WordResponseDTO } from '@/shared/types/word'
import { authAxios } from './index'

export const wordService = {
	getWordsByLevel: async (levelId: number): Promise<WordResponseDTO[]> => {
		const response = await authAxios.get<WordResponseDTO[]>(
			`/api/words/level/${levelId}`
		)
		return response.data
	},

	getWordById: async (id: number): Promise<WordResponseDTO> => {
		const response = await authAxios.get<WordResponseDTO>(`/api/words/${id}`)
		return response.data
	},

	createWord: async (data: WordRequestDTO): Promise<WordResponseDTO> => {
		const formData = new FormData()

		Object.entries(data).forEach(([key, value]) => {
			if (value instanceof File) {
				formData.append(key, value)
			} else if (value !== undefined) {
				const stringValue =
					typeof value === 'boolean' ? value.toString() : String(value)
				formData.append(key, stringValue)
			}
		})

		const response = await authAxios.post<WordResponseDTO>(
			'/api/words',
			formData,
			{
				headers: { 'Content-Type': 'multipart/form-data' },
			}
		)
		return response.data
	},

	updateWord: async (
		id: number,
		data: WordRequestDTO
	): Promise<WordResponseDTO> => {
		const formData = new FormData()

		Object.entries(data).forEach(([key, value]) => {
			if (value instanceof File) {
				formData.append(key, value)
			} else if (value !== undefined) {
				const stringValue =
					typeof value === 'boolean' ? value.toString() : String(value)
				formData.append(key, stringValue)
			}
		})

		const response = await authAxios.put<WordResponseDTO>(
			`/api/words/${id}`,
			formData,
			{
				headers: { 'Content-Type': 'multipart/form-data' },
			}
		)
		return response.data
	},

	deleteWord: async (id: number): Promise<void> => {
		await authAxios.delete(`/api/words/${id}`)
	},
}
