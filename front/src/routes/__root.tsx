import { createRootRoute, Outlet } from "@tanstack/react-router";

import { Toaster } from "@/components/ui/sonner";

export const Route = createRootRoute({
  component: () => (
    <div>
      <Outlet />
      <Toaster />
    </div>
  ),
});
