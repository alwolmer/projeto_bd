import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute('/dashboard/employee')({
  component: () => <div>Hello /dashboard/employee!</div>
})