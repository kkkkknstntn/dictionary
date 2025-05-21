import { ThemeProvider } from '@/app/providers/ThemeProvider'
import 'antd/dist/reset.css'
import { RouterProvider } from 'react-router-dom'
import { router } from './router'

export const App = () => {
	return (
		<ThemeProvider>
			<RouterProvider router={router} />
		</ThemeProvider>
	)
}

export default App
