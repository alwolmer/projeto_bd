import { productsFetch } from "@/api/queries";
import { columns } from "@/components/product/columns";
import { ProductDataTable } from "@/components/product/data-table";
import { useAxios } from "@/lib/use-axios";
import { useQuery } from "@tanstack/react-query";
import { createLazyFileRoute } from "@tanstack/react-router";

export const Route = createLazyFileRoute("/dashboard/product")({
  component: ProductTable,
});

function ProductTable() {
  const api = useAxios();

  const { isPending, data, error } = useQuery({
    queryKey: ["products"],
    queryFn: () => productsFetch(api),
  });

  if (isPending) return <div>Loading...</div>;

  if (error) {
    return <div>Error</div>;
  }

  return (
    <>
      <h1 className="text-2xl font-semibold">Products</h1>
      <div className="container mx-auto py-10">
        <ProductDataTable columns={columns} data={data} />
      </div>
    </>
  );
}
