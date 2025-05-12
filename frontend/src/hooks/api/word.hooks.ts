import { wordService } from '@/services/api/word.service'
import { QUERY_KEYS } from '@/shared/constants/queryKeys'
import type { WordRequestDTO } from '@/shared/types/word'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

export const useWordsByLevel = (levelId: number) => {
	return useQuery({
		queryKey: [...QUERY_KEYS.WORDS, levelId],
		queryFn: () => wordService.getWordsByLevel(levelId),
		enabled: !!levelId,
	})
}

export const useWordDetails = (id: number) => {
	return useQuery({
		queryKey: QUERY_KEYS.WORD_DETAILS(id),
		queryFn: () => wordService.getWordById(id),
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
