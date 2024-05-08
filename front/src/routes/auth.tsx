import { useAuthStore } from "@/store/auth-store";
import {
  Link,
  Outlet,
  createFileRoute,
  redirect,
} from "@tanstack/react-router";
import { toast } from "sonner";

export const Route = createFileRoute("/auth")({
  beforeLoad: async ({ location }) => {
    const token = useAuthStore.getState().token;
    if (token === null) {
      toast.error("You need to login first");
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
      <div className="p-2 flex gap-2">
        <Link
          to="/"
          className="[&.active]:font-bold flex flex-row items-center"
        >
          <img src="/logo.svg" alt="logo" className="w-10 h-10" />
          Armazém do Seu Zé
        </Link>{" "}
      </div>
      <hr />
      <Outlet />
    </>
  ),
});
