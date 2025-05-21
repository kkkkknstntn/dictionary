import { useCourseProgress } from '@/hooks/api/progress.hooks'
import { Card, Progress, Skeleton, Typography } from 'antd'

interface CourseProgressProps {
	courseId: number | undefined
}

export const CourseProgress = ({ courseId }: CourseProgressProps) => {
	const { data: progress, isLoading } = useCourseProgress(courseId)

	if (!courseId) {
		return null
	}

	if (isLoading) {
		return <Skeleton active />
	}

	const progressPercent = Math.round(progress?.averageProgress || 0)

	return (
		<Card title='Ваш прогресс по курсу' className='course-progress'>
			<div className='progress-content'>
				<Progress
					type='circle'
					percent={progressPercent}
					format={percent => `${percent}%`}
					width={200}
					strokeColor={{
						'0%': '#108ee9',
						'100%': '#87d068',
					}}
				/>
				<Typography.Paragraph className='progress-description'>
					Вы успешно прошли {progressPercent}% курса. Продолжайте в том же духе!
				</Typography.Paragraph>
			</div>
		</Card>
	)
}
