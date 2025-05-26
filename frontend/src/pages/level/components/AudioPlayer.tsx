import { SoundOutlined } from '@ant-design/icons'
import { Button } from 'antd'
import { forwardRef, useImperativeHandle, useEffect, useRef } from 'react'
import './LearningExercise.scss'

type Props = {
	src?: string
}

const AudioPlayerComponent = ({ src }: Props, ref: any) => {
	const audioRef = useRef<HTMLAudioElement | null>(null)

	// когда src меняется — создаём новый объект Audio
	useEffect(() => {
		if (src) {
			audioRef.current = new Audio(src)
		} else {
			audioRef.current = null
		}
	}, [src])

	useImperativeHandle(ref, () => ({
		play() {
			if (audioRef.current) {
				audioRef.current.play()
			}
		},
		stop() {
			if (audioRef.current) {
				audioRef.current.pause()
				audioRef.current.currentTime = 0
			}
		},
	}))

	const handlePlay = () => {
		if (audioRef.current) {
			audioRef.current.play()
		}
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

export const AudioPlayer = forwardRef(AudioPlayerComponent)
