import { createLazyFileRoute } from "@tanstack/react-router";

export const Route = createLazyFileRoute("/dashboard/product")({
  component: () => <div>Hello /dashboard/product!</div>,
});
