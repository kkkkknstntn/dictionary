import { useCurrentUser } from '@/hooks/api/user.hooks'
import { UserOutlined } from '@ant-design/icons'
import { Avatar, Card, Skeleton, Tabs, Typography } from 'antd'
import './ProfilePage.scss'
import { ProfileForm } from './components/ProfileForm'
import { UserCourses } from './components/UserCourses'

const { Title } = Typography
const { TabPane } = Tabs

export const ProfilePage = () => {
	const { data: user, isLoading } = useCurrentUser()

	return (
		<div className='profile-page'>
			<Card className='profile-header'>
				{isLoading ? (
					<Skeleton active />
				) : (
					<div className='profile-info'>
						<Avatar
							size={100}
							src={user?.imagePath}
							icon={<UserOutlined />}
							className='profile-avatar'
						/>
						<Title level={2}>{user?.username}</Title>
					</div>
				)}
			</Card>

			<Tabs defaultActiveKey='1' className='profile-tabs'>
				<TabPane tab='Мои курсы' key='1'>
					<UserCourses />
				</TabPane>

				<TabPane tab='Настройки' key='2'>
					<ProfileForm user={user || undefined} />
				</TabPane>
			</Tabs>
		</div>
	)
}
