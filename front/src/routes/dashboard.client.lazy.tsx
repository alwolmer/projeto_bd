import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute('/dashboard/client')({
  component: () => <div>Hello /dashboard/client!</div>
})