import { useIsUserInCourse, useJoinCourse } from '@/hooks/api/course.hooks'
import { Button, notification } from 'antd'

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
		return <Button disabled>Вы уже подписаны на курс</Button>
	}

	return (
		<Button type='primary' loading={isPending} onClick={handleJoin}>
			Присоединиться
		</Button>
	)
}
