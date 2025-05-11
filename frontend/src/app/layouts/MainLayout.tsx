import { Layout, Menu } from 'antd'
import { Link, Outlet } from 'react-router-dom'
import './MainLayout.scss'

const { Header, Sider, Content } = Layout

export const MainLayout = () => {
	return (
		<Layout className='main-layout'>
			<Sider theme='light' width={250} breakpoint='lg' collapsedWidth='0'>
				<div className='logo'>Course Platform</div>
				<Menu mode='inline' defaultSelectedKeys={['1']}>
					<Menu.Item key='1'>
						<Link to='/courses'>Все курсы</Link>
					</Menu.Item>
					<Menu.Item key='2'>
						<Link to='/profile'>Мой профиль</Link>
					</Menu.Item>
				</Menu>
			</Sider>
			<Layout>
				<Header className='header' />
				<Content className='content'>
					<Outlet />
				</Content>
			</Layout>
		</Layout>
	)
}
