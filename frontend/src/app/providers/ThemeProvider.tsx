import type { ThemeType } from '@/shared/theme/theme'
import { THEMES } from '@/shared/theme/theme'
import { ConfigProvider } from 'antd'
import type { ReactNode } from 'react'
import { useEffect, useState } from 'react'

interface ThemeProviderProps {
	children: ReactNode
}

export const ThemeProvider = ({ children }: ThemeProviderProps) => {
	const [currentTheme, setCurrentTheme] = useState<ThemeType>('light')

	useEffect(() => {
		// Проверяем сохраненную тему в localStorage
		const savedTheme = localStorage.getItem('theme') as ThemeType
		if (savedTheme) {
			setCurrentTheme(savedTheme)
		} else {
			// Проверяем системные настройки
			const prefersDark = window.matchMedia(
				'(prefers-color-scheme: dark)'
			).matches
			setCurrentTheme(prefersDark ? 'dark' : 'light')
		}
	}, [])

	return (
		<ConfigProvider theme={THEMES[currentTheme]}>{children}</ConfigProvider>
	)
}
