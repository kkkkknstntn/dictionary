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
