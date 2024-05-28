import { addressFetch } from "@/api/queries";
import { columns } from "@/components/address/columns";
import { AddressDataTable } from "@/components/address/data-table";
import { useAxios } from "@/lib/use-axios";
import { useQuery } from "@tanstack/react-query";
import { createLazyFileRoute } from "@tanstack/react-router";

export const Route = createLazyFileRoute("/dashboard/address")({
  component: AddressTable,
});

function AddressTable() {
  const api = useAxios();

  const { isPending, data, error } = useQuery({
    queryKey: ["addresses"],
    queryFn: () => addressFetch(api),
  });

  if (isPending) return <div>Loading...</div>;

  if (error) {
    return <div>Error</div>;
  }

  return (
    <>
      <h1 className="text-2xl font-semibold">Delivery Address</h1>
      <div className="container mx-auto py-10">
        <AddressDataTable columns={columns} data={data} />
      </div>
    </>
  );
}
