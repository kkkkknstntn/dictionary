import type { ThemeConfig } from 'antd'

const baseTheme = {
	token: {
		fontFamily: "'JetBrains Mono', monospace",
		borderRadius: 8,
		// Основные цвета
		colorPrimary: '#FF6B35', // Оранжевый
		colorInfo: '#4ECDC4', // Бирюзовый
		colorSuccess: '#2ECC71', // Зеленый
		colorWarning: '#FFB400', // Желтый
		colorError: '#FF6B6B', // Красный

		// Нейтральные цвета
		colorTextBase: '#2D3436',
		colorBgBase: '#FFFFFF',
		colorBgContainer: '#FFFFFF',
		colorBgElevated: '#FFFFFF',
		colorBgLayout: '#F8F9FA',

		// Цвета компонентов
		colorBorder: '#E9ECEF',
		colorBorderSecondary: '#F1F3F5',

		// Размеры
		fontSize: 14,
		sizeUnit: 4,
		sizeStep: 4,

		// Тени
		boxShadow: '0 2px 8px rgba(0, 0, 0, 0.08)',
		boxShadowSecondary: '0 4px 12px rgba(0, 0, 0, 0.12)',

		// Анимации
		motion: true,
		motionBase: 0.2,
		motionEaseInOut: 'cubic-bezier(0.4, 0, 0.2, 1)',
		motionEaseOut: 'cubic-bezier(0, 0, 0.2, 1)',
		motionEaseIn: 'cubic-bezier(0.4, 0, 1, 1)',
	},
	components: {
		Button: {
			borderRadius: 8,
			controlHeight: 40,
			paddingContentHorizontal: 24,
			fontWeight: 500,
		},
		Card: {
			borderRadius: 16,
			boxShadow: '0 4px 12px rgba(0, 0, 0, 0.08)',
		},
		Input: {
			borderRadius: 8,
			controlHeight: 40,
		},
		Select: {
			borderRadius: 8,
			controlHeight: 40,
		},
		Modal: {
			borderRadius: 16,
		},
		Drawer: {
			borderRadius: 16,
		},
		Menu: {
			borderRadius: 8,
		},
		Dropdown: {
			borderRadius: 8,
		},
		Tag: {
			borderRadius: 6,
		},
		Table: {
			borderRadius: 8,
		},
	},
}

export const lightTheme: ThemeConfig = {
	...baseTheme,
	token: {
		...baseTheme.token,
		// Дополнительные настройки для светлой темы
		colorBgContainer: '#FFFFFF',
		colorBgLayout: '#F8F9FA',
		colorTextBase: '#2D3436',
	},
}

export const darkTheme: ThemeConfig = {
	...baseTheme,
	token: {
		...baseTheme.token,
		// Настройки для темной темы
		colorPrimary: '#FF6B35',
		colorBgContainer: '#1A1A1A',
		colorBgLayout: '#141414',
		colorTextBase: '#FFFFFF',
		colorBorder: '#303030',
		colorBorderSecondary: '#1F1F1F',
	},
}

export const THEMES = {
	light: lightTheme,
	dark: darkTheme,
} as const

export type ThemeType = keyof typeof THEMES
