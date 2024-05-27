import { employeesFetch } from "@/api/queries";
import { columns } from "@/components/employee/columns";
import { EmployeeDataTable } from "@/components/employee/data-table";
import { useAxios } from "@/lib/use-axios";
import { Employee } from "@/types/auth";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { createLazyFileRoute } from "@tanstack/react-router";

export const Route = createLazyFileRoute("/dashboard/employee")({
  component: EmployeeTable,
});

function EmployeeTable() {
  const api = useAxios();

  const queryClient = useQueryClient();

  const { isPending, data, error } = useQuery({
    queryKey: ["employees"],
    queryFn: () => employeesFetch(api),
  });

  if (isPending) return <div>Loading...</div>;

  if (error) {
    return <div>Error</div>;
  }

  const me: Employee | undefined = queryClient.getQueryData(["currentUser"]);

  return (
    <>
      <h1 className="text-2xl font-semibold">Employees</h1>
      <div className="container mx-auto py-10">
        <EmployeeDataTable
          columns={columns}
          data={data}
          creationDisabled={me?.isManager ? false : true}
        />
      </div>
    </>
  );
}
