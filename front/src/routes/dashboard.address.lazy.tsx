import { createLazyFileRoute } from '@tanstack/react-router'

export const Route = createLazyFileRoute('/dashboard/address')({
  component: () => <div>Hello /dashboard/address!</div>
})