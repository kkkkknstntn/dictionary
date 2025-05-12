import { Progress, Typography } from 'antd'
import '../UserProgressPage'

type Props = {
	title: string
	percent: number
}

export const ProgressRadialChart = ({ title, percent }: Props) => (
	<div className='radial-chart'>
		<Typography.Text strong>{title}</Typography.Text>
		<Progress
			type='dashboard'
			percent={percent}
			strokeColor='#4F46E5'
			format={() => `${Math.round(percent)}%`}
			width={150}
		/>
	</div>
)
