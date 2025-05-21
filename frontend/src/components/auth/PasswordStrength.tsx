import { useEffect, useState } from 'react'

interface PasswordStrengthProps {
	password: string
}

type StrengthLevel = 'weak' | 'medium' | 'strong'

export const PasswordStrength = ({ password }: PasswordStrengthProps) => {
	const [strength, setStrength] = useState<StrengthLevel>('weak')
	const [feedback, setFeedback] = useState<string>('')

	useEffect(() => {
		if (!password) {
			setStrength('weak')
			setFeedback('')
			return
		}

		let score = 0
		const feedbacks: string[] = []

		// Проверка длины
		if (password.length >= 8) {
			score += 1
		} else {
			feedbacks.push('Добавьте еще символов')
		}

		// Проверка наличия цифр
		if (/\d/.test(password)) {
			score += 1
		} else {
			feedbacks.push('Добавьте цифры')
		}

		// Проверка наличия заглавных букв
		if (/[A-Z]/.test(password)) {
			score += 1
		} else {
			feedbacks.push('Добавьте заглавные буквы')
		}

		// Проверка наличия специальных символов
		if (/[!@#$%^&*(),.?":{}|<>]/.test(password)) {
			score += 1
		} else {
			feedbacks.push('Добавьте специальные символы')
		}

		// Определение уровня надежности
		if (score <= 2) {
			setStrength('weak')
		} else if (score === 3) {
			setStrength('medium')
		} else {
			setStrength('strong')
		}

		setFeedback(feedbacks.join(', '))
	}, [password])

	const strengthText = {
		weak: 'Слабый',
		medium: 'Средний',
		strong: 'Надежный',
	}

	return (
		<div className='password-strength'>
			<div className='strength-meter'>
				<div className={`strength-meter-fill ${strength}`} />
			</div>
			<div className='strength-text'>
				{password && (
					<>
						<span
							style={{
								color:
									strength === 'weak'
										? '#ff4d4f'
										: strength === 'medium'
										? '#faad14'
										: '#52c41a',
							}}
						>
							{strengthText[strength]}
						</span>
						{feedback && ` - ${feedback}`}
					</>
				)}
			</div>
		</div>
	)
}
