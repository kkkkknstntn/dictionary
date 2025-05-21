import { WordMediaType } from '@/shared/types/word'
import { Card } from 'antd'
import './MediaPlayer.scss'

interface MediaPlayerProps {
	type: WordMediaType
	src: string
}

export const MediaPlayer = ({ type, src }: MediaPlayerProps) => {
	if (type === 'audio') {
		return (
			<Card className='media-player audio-player'>
				<audio controls className='audio-element'>
					<source src={src} type='audio/mpeg' />
					Ваш браузер не поддерживает аудио элемент.
				</audio>
			</Card>
		)
	}

	if (type === 'video') {
		return (
			<Card className='media-player video-player'>
				<video controls className='video-element'>
					<source src={src} type='video/mp4' />
					Ваш браузер не поддерживает видео элемент.
				</video>
			</Card>
		)
	}

	return null
}
