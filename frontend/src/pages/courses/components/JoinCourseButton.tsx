import { useIsUserInCourse, useJoinCourse } from '@/hooks/api/course.hooks'
import { Button, notification } from 'antd'
import './JoinCourseButton.scss'

type Props = {
	courseId: number
}

export const JoinCourseButton = ({ courseId }: Props) => {
	const { mutate, isPending } = useJoinCourse()
	const { isUserInCourse } = useIsUserInCourse(courseId)

	const handleJoin = () => {
		mutate(courseId, {
			onSuccess: () => {
				notification.success({
					message: 'Вы успешно присоединились к курсу',
				})
			},
			onError: () => {
				notification.error({
					message: 'Ошибка при присоединении к курсу',
				})
			},
		})
	}

	if (isUserInCourse) {
		return (
			<Button disabled className='join-button'>
				<span className='full-label'>Вы уже подписаны на курс</span>
				<span className='short-label'>Подписаны</span>
			</Button>
		)
	}

	return (
		<Button
			type='primary'
			loading={isPending}
		 onClick={handleJoin}
			className='join-button'
		>
			<span className='full-label'>Присоединиться</span>
			<span className='short-label'>Подписаться</span>
		</Button>
	)
}
