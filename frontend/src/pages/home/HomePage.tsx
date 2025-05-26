import { Button, Typography } from 'antd'
import { motion } from 'framer-motion'
import { Link } from 'react-router-dom'
import './HomePage.scss'

const { Title, Paragraph } = Typography

export const HomePage = () => {
	return (
		<div className='home-page'>
			<motion.div
				initial={{ opacity: 0, y: 20 }}
				animate={{ opacity: 1, y: 0 }}
				transition={{ duration: 0.8 }}
				className='hero-section'
			>
				<Title level={1} className='main-title'>
					Добро пожаловать в Course Platform
				</Title>
				<Paragraph className='subtitle'>
					Изучайте новые языки, развивайтесь и достигайте своих целей вместе с
					нами
				</Paragraph>
				<motion.div
					initial={{ opacity: 0 }}
					animate={{ opacity: 1 }}
					transition={{ delay: 0.5 }}
					className='cta-buttons'
				>
					<Link to='/register'>
						<Button type='primary' size='large'>
							Начать обучение
						</Button>
					</Link>
					<Link to='/login'>
						<Button size='large'>Войти</Button>
					</Link>
				</motion.div>
			</motion.div>

			<motion.div
				initial={{ opacity: 0 }}
				animate={{ opacity: 1 }}
				transition={{ delay: 0.8 }}
				className='features-section'
			>
				<div className='feature-card'>
					<motion.div whileHover={{ scale: 1.05 }} className='feature-icon'>
						📚
					</motion.div>
					<Title level={3}>Разнообразные курсы</Title>
					<Paragraph>
						Выбирайте из множества курсов или создавайте свои собственные
					</Paragraph>
				</div>

				<div className='feature-card'>
					<motion.div whileHover={{ scale: 1.05 }} className='feature-icon'>
						🎯
					</motion.div>
					<Title level={3}>Отслеживание прогресса</Title>
					<Paragraph>
						Следите за своим прогрессом и видите свой рост в обучении
					</Paragraph>
				</div>

				<div className='feature-card'>
					<motion.div whileHover={{ scale: 1.05 }} className='feature-icon'>
						🌟
					</motion.div>
					<Title level={3}>Интерактивное обучение</Title>
					<Paragraph>
						Учитесь через практику с помощью интерактивных упражнений и тестов
					</Paragraph>
				</div>
			</motion.div>
		</div>
	)
}
