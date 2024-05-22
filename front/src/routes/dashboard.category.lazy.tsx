import { categoriesFetch } from "@/api/queries";
import { columns } from "@/components/category/columns";
import { CategoryDataTable } from "@/components/category/data-table";
import { useAxios } from "@/lib/use-axios";
import { useQuery } from "@tanstack/react-query";
import { createLazyFileRoute } from "@tanstack/react-router";

export const Route = createLazyFileRoute("/dashboard/category")({
  component: CategoryTable,
});

function CategoryTable() {
  const api = useAxios();

  const { isPending, data, error } = useQuery({
    queryKey: ["categories"],
    queryFn: () => categoriesFetch(api),
  });

  if (isPending) return <div>Loading...</div>;

  if (error) {
    return <div>Error</div>;
  }

  return (
    <>
      <h1 className="text-2xl font-semibold">Categories</h1>
      <div className="container mx-auto py-10">
        <CategoryDataTable columns={columns} data={data} />
      </div>
    </>
  );
}
