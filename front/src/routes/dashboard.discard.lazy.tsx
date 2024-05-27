import { discardsFetch } from "@/api/queries";
import { columns } from "@/components/discard/columns";
import { DiscardDataTable } from "@/components/discard/data-table";
import { useAxios } from "@/lib/use-axios";
import { useQuery } from "@tanstack/react-query";
import { createLazyFileRoute } from "@tanstack/react-router";

export const Route = createLazyFileRoute("/dashboard/discard")({
  component: DiscardTable,
});

function DiscardTable() {
  const api = useAxios();

  const { isPending, data, error } = useQuery({
    queryKey: ["discards"],
    queryFn: () => discardsFetch(api),
  });

  if (isPending) return <div>Loading...</div>;

  if (error) {
    return <div>Error</div>;
  }

  return (
    <>
      <h1 className="text-2xl font-semibold">Discarded Items</h1>
      <div className="container mx-auto py-10">
        <DiscardDataTable columns={columns} data={data} />
      </div>
    </>
  );
}
