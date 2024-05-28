import { clientStatsFetch, stockStatsFetch } from "@/api/queries";
import { useAxios } from "@/lib/use-axios";
import { useQuery } from "@tanstack/react-query";
import { createLazyFileRoute } from "@tanstack/react-router";
import {
  LineChart,
  Line,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  BarChart,
  Bar,
} from "recharts";

export const Route = createLazyFileRoute("/dashboard/home")({
  component: Dashboard,
});

function Dashboard() {
  const api = useAxios();

  const {
    isPending: isClientStatsPending,
    data: clientStats,
    error: clientStatsError,
  } = useQuery({
    queryKey: ["clientStats"],
    queryFn: () => clientStatsFetch(api),
  });

  const {
    isPending: isStockStatsPending,
    data: stockStats,
    error: stockStatsError,
  } = useQuery({
    queryKey: ["stockStats"],
    queryFn: () => stockStatsFetch(api),
  });

  if (isClientStatsPending || isStockStatsPending) return <div>Loading...</div>;

  if (clientStatsError || stockStatsError) {
    return <div>Error</div>;
  }

  const clientStatsData = clientStats.map((item) => ({
    name: item.clientType,
    value: item.clientCount,
  }));

  return (
    <>
      <h1 className="text-2xl font-semibold">Dashboard</h1>
      <div className="flex">
        <div>
          <h1>Clients per type</h1>
          <BarChart
            width={500}
            height={300}
            data={clientStatsData}
            margin={{ top: 20, right: 30, bottom: 5, left: 20 }}
          >
            <Bar dataKey="value" fill="#8884d8" />
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
          </BarChart>
        </div>
        <div>
          <h1>Stock Evolution</h1>
          <LineChart
            width={500}
            height={300}
            data={stockStats}
            margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="date" />
            <Tooltip />
            <Line
              type="monotone"
              dataKey="itemCount"
              stroke="#8884d8"
              activeDot={{ r: 8 }}
            />
          </LineChart>
        </div>
      </div>
    </>
  );
}
