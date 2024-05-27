import { carriersFetch } from "@/api/queries";
import { columns } from "@/components/carrier/columns";
import { CarrierDataTable } from "@/components/carrier/data-table";
import { useAxios } from "@/lib/use-axios";
import { useQuery } from "@tanstack/react-query";
import { createLazyFileRoute } from "@tanstack/react-router";

export const Route = createLazyFileRoute("/dashboard/carrier")({
  component: CarrierTable,
});

function CarrierTable() {
  const api = useAxios();

  const { isPending, data, error } = useQuery({
    queryKey: ["carriers"],
    queryFn: () => carriersFetch(api),
  });

  if (isPending) return <div>Loading...</div>;

  if (error) {
    return <div>Error</div>;
  }

  return (
    <>
      <h1 className="text-2xl font-semibold">Carriers</h1>
      <div className="container mx-auto py-10">
        <CarrierDataTable columns={columns} data={data} />
        {/* <SupplierDataTable columns={columns} data={data} /> */}
      </div>
    </>
  );
}
