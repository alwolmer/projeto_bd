import { userFetch } from "@/api/queries";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useAxios } from "@/lib/use-axios";
import { useAuthStore } from "@/store/auth-store";
import { useQuery } from "@tanstack/react-query";
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
  component: Navbar,
});

function Navbar() {
  const api = useAxios();
  const { isPending, data, error } = useQuery(userFetch(api));

  if (isPending) return <div>Loading...</div>;

  if (error) {
    toast.error(error.message);
    return <div>Error</div>;
  }
  // TODO: Ajeitar Avatar
  return (
    <>
      <div className="p-2 flex gap-2 justify-between px-12">
        <Link
          to="/"
          className="[&.active]:font-bold flex flex-row items-center"
        >
          <img src="/logo.svg" alt="logo" className="w-10 h-10" />
          Armazém do Seu Zé
        </Link>{" "}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button
              variant="outline"
              size="icon"
              className="overflow-hidden rounded-full"
            >
              <Avatar>
                <AvatarFallback>{data?.name[0]}</AvatarFallback>
              </Avatar>
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent>
            <DropdownMenuItem>Logout</DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
      <hr />
      <Outlet />
    </>
  );
}
