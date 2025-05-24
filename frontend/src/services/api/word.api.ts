import type { WordRequestDTO, WordResponseDTO } from '@/shared/types/word'
import { authAxios } from './index'

export const wordApi = {
	getWords: (levelId?: number) =>
		authAxios.get<WordResponseDTO[]>('/api/words', {
			params: { levelId },
		}),

	getWordDetails: (id: number) =>
		authAxios.get<WordResponseDTO>(`/api/words/${id}`),

	createWord: (data: WordRequestDTO) =>
		authAxios.post<WordResponseDTO>('/api/words', data),

	updateWord: (id: number, formData: FormData) =>
		authAxios.put<WordResponseDTO>(`/api/words/${id}`, formData, {
			headers: {
				'Content-Type': 'multipart/form-data',
			},
		}),

	deleteWord: (id: number) => authAxios.delete(`/api/words/${id}`),
}
