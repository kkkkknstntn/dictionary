import { authAxios } from '@/services/api'
import { wordApi } from '@/services/api/word.api'
import { wordService } from '@/services/api/word.service'
import { QUERY_KEYS } from '@/shared/constants/queryKeys'
import type { LevelDTO, WordRequestDTO } from '@/shared/types/word'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'

export const useWordsByLevel = (levelId: number) => {
	return useQuery({
		queryKey: ['words', 'level', levelId],
		queryFn: () => wordService.getWordsByLevel(levelId),
		enabled: !!levelId,
	})
}

export const useWordDetails = (wordId: number) => {
	return useQuery({
		queryKey: ['word', wordId],
		queryFn: () => wordService.getWordById(wordId),
		enabled: !!wordId,
	})
}

export const useLevelDetails = (levelId: number) => {
	return useQuery({
		queryKey: ['level', levelId],
		queryFn: async () => {
			const response = await authAxios.get<LevelDTO>(`/api/levels/${levelId}`)
			return response.data
		},
		enabled: !!levelId,
	})
}

export const useCreateWord = () => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: (data: WordRequestDTO) => wordService.createWord(data),
		// eslint-disable-next-line @typescript-eslint/no-unused-vars
		onSuccess: (_, _variables) => {
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.WORDS })
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.LEVELS })
		},
	})
}

export const useUpdateWord = () => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: ({ id, formData }: { id: number; formData: FormData }) =>
			wordApi.updateWord(id, formData),
		onSuccess: (_, { id }) => {
			queryClient.invalidateQueries({
				queryKey: QUERY_KEYS.WORD_DETAILS(id),
			})
		},
	})
}

export const useDeleteWord = () => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: (id: number) => wordApi.deleteWord(id),
		onSuccess: (_, id) => {
			queryClient.invalidateQueries({
				queryKey: QUERY_KEYS.WORD_DETAILS(id),
			})
		},
	})
}

export const useWordNavigation = (currentWordId: number, levelId: number) => {
	const { data: words } = useWordsByLevel(levelId)
	const navigate = useNavigate()

	const currentIndex = words?.findIndex(w => w.id === currentWordId) ?? -1
	const canGoNext =
		currentIndex !== -1 && currentIndex < (words?.length ?? 0) - 1
	const canGoPrevious = currentIndex > 0

	const goToNextWord = () => {
		if (canGoNext && words) {
			navigate(`/word/${words[currentIndex + 1].id}`)
		}
	}

	const goToPreviousWord = () => {
		if (canGoPrevious && words) {
			navigate(`/word/${words[currentIndex - 1].id}`)
		}
	}

	return {
		canGoNext,
		canGoPrevious,
		goToNextWord,
		goToPreviousWord,
	}
}
