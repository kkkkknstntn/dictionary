import { Layout, Menu } from 'antd'
import { Link, Outlet, useLocation } from 'react-router-dom'
import './MainLayout.scss'

const { Header, Content } = Layout

export const MainLayout = () => {
	const location = useLocation()
	const currentPath = location.pathname

	return (
		<Layout className='main-layout'>
			<Header className='header'>
				<div className='header-content'>
					<div className='logo'>Course Platform</div>
					<Menu
						mode='horizontal'
						selectedKeys={[currentPath]}
						items={[
							{
								key: '/courses',
								label: <Link to='/courses'>Все курсы</Link>,
							},
							{
								key: '/profile',
								label: <Link to='/profile'>Мой профиль</Link>,
							},
						]}
					/>
				</div>
			</Header>
			<Content className='content'>
				<Outlet />
			</Content>
		</Layout>
	)
}
