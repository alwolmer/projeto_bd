// import { useAxios } from "@/lib/use-axios";
import { useAuthStore } from "@/store/auth-store";
import { createFileRoute, redirect } from "@tanstack/react-router";
import { toast } from "sonner";
// import { useQuery } from "@tanstack/react-query";
// import { userFetch } from "@/api/queries";

export const Route = createFileRoute("/")({
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
    throw redirect({
      to: "/dashboard",
    });
  },
});

// function Index() {
//   const api = useAxios();

//   const { isPending, error, data } = useQuery(userFetch(api));

//   if (isPending) {
//     return <div>Loading...</div>;
//   }

//   if (error) {
//     return <div>Error: {error.message}</div>;
//   }

//   return (
//     <div className="p-2">
//       <h3>{data.name}</h3>
//     </div>
//   );
// }
