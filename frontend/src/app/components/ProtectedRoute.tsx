import { useCurrentUser } from '@/hooks/api/user.hooks'
import type { JSX } from 'react'
import { Navigate, useLocation } from 'react-router-dom'

export const ProtectedRoute = ({ children }: { children: JSX.Element }) => {
	const { data: user } = useCurrentUser()
	const location = useLocation()

	if (!user) {
		return <Navigate to='/login' state={{ from: location }} replace />
	}

	return children
}
