import 'antd/dist/reset.css'

//import { MainLayout } from '@/app/layouts/MainLayout'
import { ActivatePage } from '@/pages/auth/ActivatePage'
import { LoginPage } from '@/pages/auth/LoginPage'
import { RegisterPage } from '@/pages/auth/RegisterPage'
import { Route, Routes } from 'react-router-dom'

export const App = () => (
	// <BrowserRouter>
	<Routes>
		<Route path='/login' element={<LoginPage />} />
		<Route path='/register' element={<RegisterPage />} />
		<Route path='/activate-account' element={<ActivatePage />} />

		{/* <Route element={<MainLayout />}>
				<Route path='/courses' element={<div />} />
			</Route> */}
	</Routes>
	// </BrowserRouter>
)

export default App
