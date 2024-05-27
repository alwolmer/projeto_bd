import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute('/dashboard/item')({
  component: () => <div>Hello /dashboard/item!</div>
})