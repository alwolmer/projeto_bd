import { suppliersFetch } from "@/api/queries";
import { columns } from "@/components/supplier/columns";
import { SupplierDataTable } from "@/components/supplier/data-table";
import { useAxios } from "@/lib/use-axios";
import { useQuery } from "@tanstack/react-query";
import { createLazyFileRoute } from "@tanstack/react-router";

export const Route = createLazyFileRoute("/dashboard/supplier")({
  component: SupplierTable,
});

function SupplierTable() {
  const api = useAxios();

  const { isPending, data, error } = useQuery({
    queryKey: ["suppliers"],
    queryFn: () => suppliersFetch(api),
  });

  if (isPending) return <div>Loading...</div>;

  if (error) {
    return <div>Error</div>;
  }

  return (
    <>
      <h1 className="text-2xl font-semibold">Suppliers</h1>
      <div className="container mx-auto py-10">
        <SupplierDataTable columns={columns} data={data} />
      </div>
    </>
  );
}
