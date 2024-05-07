import { Link, Outlet, createFileRoute } from "@tanstack/react-router";

export const Route = createFileRoute("/dashboard/")({
  component: () => (
    <>
      <div className="p-2 flex gap-2">
        <Link
          to="/"
          className="[&.active]:font-bold flex flex-row items-center"
        >
          <img src="/logo.svg" alt="logo" className="w-10 h-10" />
          Armazém do Seu Zé
        </Link>{" "}
      </div>
      <hr />
      <Outlet />
    </>
  ),
});
