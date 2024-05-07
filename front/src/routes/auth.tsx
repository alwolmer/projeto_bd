import { useAuthStore } from "@/store/auth-store";
import { Outlet, createFileRoute, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/auth")({
  beforeLoad: async ({ location }) => {
    const token = useAuthStore.getState().token;
    if (token === null) {
      throw redirect({
        to: "/login",
        search: {
          redirect: location.pathname,
        },
      });
    }
  },
  component: () => (
    <>
      <div>Hehe</div>
      <Outlet />
    </>
  ),
});
