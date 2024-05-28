import { clientsFetch } from "@/api/queries";
import { columns } from "@/components/client/columns";
import { ClientDataTable } from "@/components/client/data-table";
import { useAxios } from "@/lib/use-axios";
import { useQuery } from "@tanstack/react-query";
import { createLazyFileRoute } from "@tanstack/react-router";

export const Route = createLazyFileRoute("/dashboard/client")({
  component: ClientTable,
});

function ClientTable() {
  const api = useAxios();

  const { isPending, data, error } = useQuery({
    queryKey: ["clients"],
    queryFn: () => clientsFetch(api),
  });

  if (isPending) return <div>Loading...</div>;

  if (error) {
    return <div>Error</div>;
  }

  return (
    <>
      <h1 className="text-2xl font-semibold">Clients</h1>
      <div className="container mx-auto py-10">
        {/* <CarrierDataTable columns={columns} data={data} /> */}
        <ClientDataTable columns={columns} data={data} />
      </div>
    </>
  );
}
