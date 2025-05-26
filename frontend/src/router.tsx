import { ActivatePage } from '@/pages/auth/ActivatePage'
import { LoginPage } from '@/pages/auth/LoginPage'
import { RegisterPage } from '@/pages/auth/RegisterPage'
import { createBrowserRouter } from 'react-router-dom'
import { MainLayout } from './app/layouts/MainLayout'
import { HomePage } from './pages/home/HomePage'
import { CoursePage } from './pages/courses/CoursePage'
import { CoursesPage } from './pages/courses/CoursesPage'
import { LevelPage } from './pages/level/LevelPage'
import { ProfilePage } from './pages/profile/ProfilePage'
import { UserProgressPage } from './pages/progress/UserProgressPage'
import { WordPage } from './pages/word/WordPage'

export const router = createBrowserRouter([
	{
		path: '/',
		element: <HomePage />,
	},
	{
		path: '/login',
		element: <LoginPage />,
	},
	{
		path: '/register',
		element: <RegisterPage />,
	},
	{
		path: '/activate-account',
		element: <ActivatePage />,
	},
	{
		element: <MainLayout />,
		children: [
			{
				path: '/courses',
				element: <CoursesPage />,
			},
			{
				path: '/course/:id',
				element: <CoursePage />,
			},
			{
				path: '/level/:id',
				element: <LevelPage />,
			},
			{
				path: '/word/:id',
				element: <WordPage />,
			},
			{
				path: '/profile',
				element: <ProfilePage />,
			},
			{
				path: '/progress',
				element: <UserProgressPage />,
			},
		],
	},
])
