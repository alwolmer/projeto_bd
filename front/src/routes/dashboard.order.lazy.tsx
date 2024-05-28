import { ordersFetch } from "@/api/queries";
import { columns } from "@/components/order/columns";
import { OrderDataTable } from "@/components/order/data-table";
import { useAxios } from "@/lib/use-axios";
import { useQuery } from "@tanstack/react-query";
import { createLazyFileRoute } from "@tanstack/react-router";

export const Route = createLazyFileRoute("/dashboard/order")({
  component: OrderTable,
});

function OrderTable() {
  const api = useAxios();

  const { isPending, data, error } = useQuery({
    queryKey: ["orders"],
    queryFn: () => ordersFetch(api),
  });

  if (isPending) return <div>Loading...</div>;

  if (error) {
    return <div>Error</div>;
  }

  return (
    <>
      <h1 className="text-2xl font-semibold">Orders</h1>
      <div className="container mx-auto py-10">
        <OrderDataTable columns={columns} data={data} />
      </div>
    </>
  );
}
