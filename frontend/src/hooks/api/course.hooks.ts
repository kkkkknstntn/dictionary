import { courseService } from '@/services/api/course.service'
import { QUERY_KEYS } from '@/shared/constants/queryKeys'
import { useQuery } from '@tanstack/react-query'

export const useCourseProgressUsers = (courseId: number | undefined) => {
	return useQuery({
		queryKey: courseId ? QUERY_KEYS.COURSE_PROGRESS_USERS(courseId) : [],
		queryFn: () => courseService.getCourseUsersProgress(courseId!),
		enabled: !!courseId,
	})
}
