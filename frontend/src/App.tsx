import 'antd/dist/reset.css'

import { ActivatePage } from '@/pages/auth/ActivatePage'
import { LoginPage } from '@/pages/auth/LoginPage'
import { RegisterPage } from '@/pages/auth/RegisterPage'
import { Route, Routes } from 'react-router-dom'
import { MainLayout } from './app/layouts/MainLayout'
import { CoursePage } from './pages/course/CoursePage'
import { LevelPage } from './pages/level/LevelPage'
import { ProfilePage } from './pages/profile/ProfilePage'
import { UserProgressPage } from './pages/progress/UserProgressPage'

export const App = () => {
	return (
		<Routes>
			<Route path='/login' element={<LoginPage />} />
			<Route path='/register' element={<RegisterPage />} />
			<Route path='/activate-account' element={<ActivatePage />} />

			<Route element={<MainLayout />}>
				<Route path='/courses' element={<CoursePage />} />
				<Route path='/course/:id' element={<CoursePage />} />
				<Route path='/level/:id' element={<LevelPage />} />
				<Route path='/profile' element={<ProfilePage />} />
				<Route path='/progress' element={<UserProgressPage />} />
			</Route>
		</Routes>
	)
}

export default App
