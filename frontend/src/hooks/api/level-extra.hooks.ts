// src/hooks/api/level-extra.hooks.ts
import { courseService } from '@/services/api/course.service'
import { levelService } from '@/services/api/level.service'
import { useQuery } from '@tanstack/react-query'

export const useIsCourseAuthor = (
	levelId: number | undefined,
	currentUsername?: string
) =>
	useQuery({
		queryKey: ['is-course-author', levelId, currentUsername],
		enabled: !!levelId && !!currentUsername,
		queryFn: async () => {
			const level = await levelService.getLevelById(levelId!)
			const course = await courseService.getCourseById(level.courseId)
			return currentUsername === course.author.username
		},
	})
