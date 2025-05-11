import { userService } from '@/services/api/user.service'
import { QUERY_KEYS } from '@/shared/constants/queryKeys'
import type { UserResponseDTO, UserUpdateData } from '@/shared/types/user'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
export const useCurrentUser = () => {
	return useQuery<UserResponseDTO | null>({
		queryKey: QUERY_KEYS.CURRENT_USER,
		queryFn: async () => {
			try {
				return await userService.getCurrentUser()
			} catch (error) {
				console.log(error)
				return null
			}
		},
		staleTime: Infinity,
	})
}

export const useUpdateUser = (id: number) => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: (data: UserUpdateData) => userService.updateUser(id, data),
		onSuccess: updatedUser => {
			queryClient.setQueryData(QUERY_KEYS.CURRENT_USER, updatedUser)
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.USERS })
		},
	})
}
