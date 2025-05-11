import {
	useCourseProgress,
	useLevelProgress,
	useWordProgress,
} from '@/hooks/api/progress.hooks'
import { Col, Row, Tabs, Typography } from 'antd'
import './UserProgressPage.scss'
import { ProgressRadialChart } from './components/ProgressRadialChart'

const { Title } = Typography
const { TabPane } = Tabs

export const UserProgressPage = () => {
	const { data: courseProgress } = useCourseProgress(1) // Пример для courseId=1
	const { data: levelProgress } = useLevelProgress(1) // Пример для levelId=1
	const { data: wordProgress } = useWordProgress(1) // Пример для wordId=1

	return (
		<div className='progress-page'>
			<Title level={2}>Детальная статистика</Title>

			<Tabs defaultActiveKey='1'>
				<TabPane tab='По курсам' key='1'>
					<Row gutter={[24, 24]}>
						<Col span={8}>
							<ProgressRadialChart
								title='Общий прогресс'
								percent={courseProgress?.averageProgress || 0}
							/>
						</Col>
					</Row>
				</TabPane>

				<TabPane tab='По уровням' key='2'>
					<div className='progress-bars'>
						<ProgressRadialChart
							title='Уровень 1'
							percent={levelProgress?.averageProgress || 0}
						/>
					</div>
				</TabPane>

				<TabPane tab='По словам' key='3'>
					<ProgressRadialChart
						title="Слово 'Example'"
						percent={wordProgress?.averageProgress || 0}
					/>
				</TabPane>
			</Tabs>
		</div>
	)
}
