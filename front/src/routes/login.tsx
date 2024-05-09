import { createFileRoute, redirect, useNavigate } from "@tanstack/react-router";
import { useForm } from "@tanstack/react-form";
// import type { FieldApi } from "@tanstack/react-form";

import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { useAuthStore } from "@/store/auth-store";
import { signIn } from "@/api/authApi";
import { toast } from "sonner";

export const Route = createFileRoute("/login")({
  beforeLoad: async () => {
    const token = useAuthStore.getState().token;
    if (token !== null) {
      throw redirect({
        to: "/",
      });
    }
  },
  component: LoginForm,
});

// function FieldInfo({ field }: { field: FieldApi<any, any, any, any> }) {
//   return (
//     <>
//       {field.state.meta.touchedErrors ? (
//         <em>{field.state.meta.touchedErrors}</em>
//       ) : null}
//       {field.state.meta.isValidating ? "Validating..." : null}
//     </>
//   );
// }

function formatCpf(value: string) {
  const cleanedValue = value.replace(/\D/g, "");

  return cleanedValue
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d{1,2})/, "$1-$2")
    .replace(/(-\d{2})\d+?$/, "$1");
}

function LoginForm() {
  const { setToken, setRefreshToken } = useAuthStore();

  const navigate = useNavigate({ from: "/login" });

  const form = useForm({
    defaultValues: {
      cpf: "",
      password: "",
    },
    onSubmit: async ({ value }) => {
      const { cpf, password } = value;
      await signIn(cpf, password)
        .then((response) => {
          setToken(response.access);
          setRefreshToken(response.refresh);
          navigate({ to: "/" });
        })
        .catch((err) => {
          toast.error(err.message);
        });
    },
  });

  return (
    <div className="min-h-screen min-w-screen justify-center items-center flex">
      <Card className="w-full max-w-sm">
        <CardHeader>
          <CardTitle className="text-2xl">Login</CardTitle>
          <CardDescription>
            Enter your email below to login to your account.
          </CardDescription>
        </CardHeader>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            e.stopPropagation();
            form.handleSubmit();
          }}
        >
          <CardContent className="grid gap-4">
            <form.Field
              name="cpf"
              validators={{
                onChange: ({ value }) =>
                  !value
                    ? "CPF is required"
                    : !value.includes(".") && !value.includes("-")
                      ? "Invalid CPF"
                      : undefined,
                onChangeAsyncDebounceMs: 500,
                onChangeAsync: async ({ value }) => {
                  await new Promise((resolve) => setTimeout(resolve, 1000));
                  return value.includes("+")
                    ? "CPF cannot contain '+'"
                    : undefined;
                },
              }}
              children={(field) => {
                return (
                  <div className="grid gap-2">
                    <Label htmlFor={field.name}>CPF</Label>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) =>
                        field.handleChange(formatCpf(e.target.value))
                      }
                      type="text"
                      placeholder="000.000.000-00"
                    />
                    {/* <FieldInfo field={field} /> */}
                  </div>
                );
              }}
            />
            <form.Field
              name="password"
              validators={{
                onChange: ({ value }) =>
                  !value ? "Password is required" : undefined,
                onChangeAsyncDebounceMs: 500,
                onChangeAsync: async () => {
                  await new Promise((resolve) => setTimeout(resolve, 1000));
                  return undefined;
                },
              }}
              children={(field) => {
                return (
                  <div className="grid gap-2">
                    <Label htmlFor={field.name}>Password</Label>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                      type="password"
                    />
                    {/* <FieldInfo field={field} /> */}
                  </div>
                );
              }}
            />
          </CardContent>
          <CardFooter>
            <form.Subscribe
              selector={(state) => [state.canSubmit, state.isSubmitting]}
              children={([canSubmit, isSubmitting]) => (
                <Button className="w-full" disabled={!canSubmit}>
                  {isSubmitting ? "Submitting..." : "Sign in"}
                </Button>
              )}
            />
          </CardFooter>
        </form>
      </Card>
    </div>
  );
}
