import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/auth/dashboard')({
  component: () => <div>Hello /auth/index/dashboard!</div>
})