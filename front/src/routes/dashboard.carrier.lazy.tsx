import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute('/dashboard/carrier')({
  component: () => <div>Hello /dashboard/carrier!</div>
})