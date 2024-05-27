import { itemsFetch } from "@/api/queries";
import { columns } from "@/components/item/columns";
import { ItemDataTable } from "@/components/item/data-table";
import { useAxios } from "@/lib/use-axios";
import { useQuery } from "@tanstack/react-query";
import { createLazyFileRoute } from "@tanstack/react-router";

export const Route = createLazyFileRoute("/dashboard/item")({
  component: ItemTable,
});

function ItemTable() {
  const api = useAxios();

  const { isPending, data, error } = useQuery({
    queryKey: ["items"],
    queryFn: () => itemsFetch(api),
  });

  if (isPending) return <div>Loading...</div>;

  if (error) {
    return <div>Error</div>;
  }

  return (
    <>
      <h1 className="text-2xl font-semibold">Items</h1>
      <div className="container mx-auto py-10">
        {/* <EmployeeDataTable
          columns={columns}
          data={data}
          creationDisabled={me?.isManager ? false : true}
        /> */}
        <ItemDataTable columns={columns} data={data} />
      </div>
    </>
  );
}
