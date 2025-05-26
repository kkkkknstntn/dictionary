import { Layout, Menu, Grid } from 'antd'
import { Link, Outlet, useLocation } from 'react-router-dom'
import './MainLayout.scss'

const { Header, Content } = Layout
const { useBreakpoint } = Grid

export const MainLayout = () => {
	const location = useLocation()
	const currentPath = location.pathname
	const screens = useBreakpoint()

	return (
		<Layout className='main-layout'>
			<Header className='header'>
				<div className='header-content'>
					{screens.md ? (
						<>
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
						</>
					) : (
						<div className='mobile-links'>
							<Link to='/courses' className={currentPath === '/courses' ? 'active' : ''}>
								Все курсы
							</Link>
							<Link to='/profile' className={currentPath === '/profile' ? 'active' : ''}>
								Мой профиль
							</Link>
						</div>
					)}
				</div>
			</Header>
			<Content className='content'>
				<Outlet />
			</Content>
		</Layout>
	)
}
