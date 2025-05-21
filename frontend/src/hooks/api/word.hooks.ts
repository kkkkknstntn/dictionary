import { wordService } from '@/services/api/word.service'
import { QUERY_KEYS } from '@/shared/constants/queryKeys'
import type { WordRequestDTO } from '@/shared/types/word'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useNavigate } from 'react-router-dom'

export const useWordsByLevel = (levelId: number) => {
	return useQuery({
		queryKey: ['words', 'level', levelId],
		queryFn: () => wordService.getWordsByLevel(levelId),
		enabled: !!levelId,
	})
}

export const useWordDetails = (id: number) => {
	return useQuery({
		queryKey: ['word', id],
		queryFn: () => wordService.getWordById(id),
		enabled: !!id,
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

export const useUpdateWord = (id: number) => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: (data: WordRequestDTO) => wordService.updateWord(id, data),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.WORDS })
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.LEVELS })
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.WORD_DETAILS(id) })
		},
	})
}

export const useDeleteWord = () => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: (id: number) => wordService.deleteWord(id),
		onSuccess: (_, id) => {
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.WORDS })
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.LEVELS })
			queryClient.removeQueries({ queryKey: QUERY_KEYS.WORD_DETAILS(id) })
		},
	})
}

export const useWordNavigation = (currentWordId: number, levelId: number) => {
	const { data: words } = useWordsByLevel(levelId)
	const navigate = useNavigate()

	const currentIndex = words?.findIndex(word => word.id === currentWordId) ?? -1

	const goToNextWord = () => {
		if (words && currentIndex < words.length - 1) {
			navigate(`/word/${words[currentIndex + 1].id}`)
		}
	}

	const goToPreviousWord = () => {
		if (words && currentIndex > 0) {
			navigate(`/word/${words[currentIndex - 1].id}`)
		}
	}

	return {
		canGoNext: currentIndex < (words?.length ?? 0) - 1,
		canGoPrevious: currentIndex > 0,
		goToNextWord,
		goToPreviousWord,
	}
}
