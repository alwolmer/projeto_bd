import { createRootRoute, Outlet } from "@tanstack/react-router";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";

import { Toaster } from "@/components/ui/sonner";

export const Route = createRootRoute({
  component: () => (
    <div>
      <Outlet />
      <Toaster />
      <ReactQueryDevtools initialIsOpen={false} />
    </div>
  ),
});
