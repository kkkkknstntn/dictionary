import { courseService } from '@/services/api/course.service'
import { QUERY_KEYS } from '@/shared/constants/queryKeys'
import type { CourseRequestDTO } from '@/shared/types/course'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

export const useUserCourses = () => {
	return useQuery({
		queryKey: QUERY_KEYS.USER_COURSES,
		queryFn: () => courseService.getUserCourses(),
	})
}

export const useIsUserInCourse = (courseId: number | undefined) => {
	const { data: userCourses } = useUserCourses()
	return {
		isUserInCourse:
			userCourses?.some(course => course.id === courseId) ?? false,
	}
}

export const useSearchCourses = (searchQuery: string) => {
	return useQuery({
		queryKey: [...QUERY_KEYS.COURSES, 'search', searchQuery],
		queryFn: () => courseService.searchCourses({ query: searchQuery }),
		enabled: searchQuery.length > 0,
	})
}

export const useCreateCourse = () => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: (data: CourseRequestDTO) => courseService.createCourse(data),
		onSuccess: newCourse => {
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.COURSES })
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.USER_COURSES })
			queryClient.setQueryData(['course-details', newCourse.id], newCourse)
		},
	})
}

export const useCourseProgressUsers = (courseId: number | undefined) => {
	return useQuery({
		queryKey: courseId ? QUERY_KEYS.COURSE_PROGRESS_USERS(courseId) : [],
		queryFn: () => courseService.getCourseUsersProgress(courseId!),
		enabled: !!courseId,
	})
}

export const useCourseDetails = (id: number) => {
	return useQuery({
		queryKey: ['course-details', id],
		queryFn: () => courseService.getCourseById(id),
		enabled: !!id,
	})
}

export const useJoinCourse = () => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: (id: number) => courseService.joinCourse(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.COURSES })
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.USER_COURSES })
		},
	})
}

export const useCourses = () => {
	return useQuery({
		queryKey: QUERY_KEYS.COURSES,
		queryFn: () => courseService.getAllCourses(),
	})
}

export const useCourseProgress = (courseId: number | undefined) => {
	return useQuery({
		queryKey: courseId ? ['courseProgress', courseId] : [],
		queryFn: () => courseService.getCourseUsersProgress(courseId!),
		enabled: !!courseId,
	})
}

export const useUpdateCourse = () => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: ({ id, data }: { id: number; data: CourseRequestDTO }) =>
			courseService.updateCourse(id, data),
		onSuccess: (_, { id }) => {
			queryClient.invalidateQueries({ queryKey: ['course-details', id] })
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.COURSES })
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.USER_COURSES })
		},
	})
}

export const useDeleteCourse = () => {
	const queryClient = useQueryClient()

	return useMutation({
		mutationFn: (id: number) => courseService.deleteCourse(id),
		onSuccess: () => {
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.COURSES })
			queryClient.invalidateQueries({ queryKey: QUERY_KEYS.USER_COURSES })
		},
	})
}
