import { userFetch } from "@/api/queries";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";
import { useAxios } from "@/lib/use-axios";
import { useAuthStore } from "@/store/auth-store";
import { useQuery } from "@tanstack/react-query";
import {
  Link,
  Outlet,
  createFileRoute,
  redirect,
  useNavigate,
} from "@tanstack/react-router";
import { Home, Menu, Package, Tag } from "lucide-react";
import { toast } from "sonner";

export const Route = createFileRoute("/dashboard")({
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
  const { setToken, setRefreshToken } = useAuthStore();

  const navigate = useNavigate({ from: window.location.pathname });

  if (isPending) return <div>Loading...</div>;

  if (error) {
    toast.error(error.message);
    return <div>Error</div>;
  }

  const signOut = () => {
    setToken(null);
    setRefreshToken(null);
    navigate({ to: "/login" });
  };

  return (
    <div className="grid min-h-screen w-full md:grid-cols-[220px_1fr] lg:grid-cols-[280px_1fr]">
      <div className="hidden border-r bg-muted/40 md:block">
        <div className="flex h-full max-h-screen flex-col gap-2">
          <div className="flex h-14 items-center border-b px-4 lg:h-[60px] lg:px-6">
            <Link
              to="/dashboard/home"
              className="flex items-center gap-2 font-semibold"
            >
              <img src="/logo.svg" alt="logo" className="w-6 h-6" />
              <span className="">Armazém do Seu Zé</span>
            </Link>
          </div>
          <div className="flex-1">
            <nav className="grid items-start px-2 text-sm font-medium lg:px-4">
              <Link
                to="/dashboard/home"
                className="flex items-center gap-3 rounded-lg px-3 py-2 text-muted-foreground transition-all hover:text-primary"
              >
                <Home className="h-4 w-4" />
                Home
              </Link>
              <Link
                to="/dashboard/category"
                className="flex items-center gap-3 rounded-lg px-3 py-2 text-muted-foreground transition-all hover:text-primary"
              >
                <Tag className="h-4 w-4" />
                Category
              </Link>
              <Link
                to="/dashboard/product"
                className="flex items-center gap-3 rounded-lg px-3 py-2 text-muted-foreground transition-all hover:text-primary"
              >
                <Package className="h-4 w-4" />
                Product
              </Link>
            </nav>
          </div>
          <div className="mt-auto p-4"></div>
        </div>
      </div>
      <div className="flex flex-col">
        <header className="flex h-14 items-center gap-4 border-b bg-muted/40 px-4 lg:h-[60px] lg:px-6">
          <Sheet>
            <SheetTrigger asChild>
              <Button
                variant="outline"
                size="icon"
                className="shrink-0 md:hidden"
              >
                <Menu className="h-5 w-5" />
                <span className="sr-only">Toggle navigation menu</span>
              </Button>
            </SheetTrigger>
            <SheetContent side="left" className="flex flex-col">
              <nav className="grid gap-2 text-lg font-medium">
                <Link
                  to="/dashboard/home"
                  className="flex items-center gap-2 text-lg font-semibold"
                >
                  <img src="/logo.svg" alt="logo" className="w-6 h-6" />
                  Armazém do Seu Zé
                </Link>
                <Link
                  to="/dashboard/home"
                  className="mx-[-0.65rem] flex items-center gap-4 rounded-xl px-3 py-2 text-muted-foreground hover:text-foreground"
                >
                  <Home className="h-4 w-4" />
                  Home
                </Link>
                <Link
                  to="/dashboard/category"
                  className="mx-[-0.65rem] flex items-center gap-4 rounded-xl px-3 py-2 text-muted-foreground hover:text-foreground"
                >
                  <Tag className="h-4 w-4" />
                  Category
                </Link>
                <Link
                  to="/dashboard/product"
                  className="mx-[-0.65rem] flex items-center gap-4 rounded-xl px-3 py-2 text-muted-foreground hover:text-foreground"
                >
                  <Package className="h-4 w-4" />
                  Product
                </Link>
              </nav>
            </SheetContent>
          </Sheet>
          <div className="w-full flex-1"></div>
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="secondary" size="icon" className="rounded-full">
                <Avatar>
                  <AvatarFallback>{data?.name[0]}</AvatarFallback>
                </Avatar>
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuLabel>{data.name}</DropdownMenuLabel>
              <DropdownMenuSeparator />
              <DropdownMenuItem onClick={() => signOut()}>
                Logout
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </header>
        <main className="flex flex-1 flex-col gap-4 p-4 lg:gap-6 lg:p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );

  // return (
  //   <>
  //     <div className="p-2 flex gap-2 justify-between px-12">
  //       <Link
  //         to="/"
  //         className="[&.active]:font-bold flex flex-row items-center"
  //       >
  //         <img src="/logo.svg" alt="logo" className="w-10 h-10" />
  //         Armazém do Seu Zé
  //       </Link>{" "}
  //       <DropdownMenu>
  //         <DropdownMenuTrigger>
  //           <Avatar>
  //             <AvatarFallback>{data?.name[0]}</AvatarFallback>
  //           </Avatar>
  //         </DropdownMenuTrigger>
  //         <DropdownMenuContent>
  //           <DropdownMenuLabel>{data.name}</DropdownMenuLabel>
  //           <Separator />
  //           <DropdownMenuItem onClick={() => signOut()}>
  //             Logout
  //           </DropdownMenuItem>
  //         </DropdownMenuContent>
  //       </DropdownMenu>
  //     </div>
  //     <hr />
  //     <Outlet />
  //   </>
  // );
}