import { SoundOutlined } from '@ant-design/icons'
import { Button } from 'antd'
import './LearningExercise.scss'

type Props = {
	src?: string
}

export const AudioPlayer = ({ src }: Props) => {
	const handlePlay = () => {
		if (!src) return
		new Audio(src).play()
	}

	return (
		<div className='audio-player'>
			<Button
				type='primary'
				shape='circle'
				icon={<SoundOutlined />}
				size='large'
				onClick={handlePlay}
			/>
		</div>
	)
}
