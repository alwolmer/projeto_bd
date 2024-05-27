import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute('/dashboard/discard')({
  component: () => <div>Hello /dashboard/discard!</div>
})