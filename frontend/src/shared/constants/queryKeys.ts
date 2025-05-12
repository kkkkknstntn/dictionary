import type { LearningParams } from '../types/learn'

export const QUERY_KEYS = {
	CURRENT_USER: ['currentUser'],
	USERS: ['users'],
	COURSES: ['courses'],
	USER_COURSES: ['user-courses'],
	COURSE_SEARCH: ['course-search'],
	LEVELS: ['levels'],
	LEVEL_DETAILS: (id: number) => ['level', id],
	WORDS: ['words'],
	WORD_DETAILS: (id: number) => ['word', id],
	LEARNING_MATERIAL: (params: LearningParams) => ['learning', params],
	COURSE_PROGRESS_USERS: (id: number) => ['course-progress-users', id],
} as const
