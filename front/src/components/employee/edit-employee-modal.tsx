import { useAxios } from "@/lib/use-axios";
import { Supplier } from "@/types/storage";
import { useForm } from "@tanstack/react-form";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { Button } from "../ui/button";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";
import { Label } from "../ui/label";
import { Input } from "../ui/input";
import { toast } from "sonner";
import { Employee } from "@/types/auth";
import { Switch } from "../ui/switch";

export const EditEmployeeModal = ({
  cpf,
  name,
  phone,
  email,
  isManager,
}: Employee) => {
  const [open, setOpen] = useState(false);

  const api = useAxios();

  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: (employee: Employee): Promise<Employee> => {
      return api
        .patch(`/employee/${cpf?.replace(/\D/g, "")}`, {
          name: employee.name,
          phone: employee.phone,
          email: employee.email,
          isManager: employee.isManager,
          password: employee.passwordHash ? employee.passwordHash : "",
        })
        .then((res) => res.data);
    },
    onSuccess: (data: Supplier) => {
      queryClient.setQueryData(["employees"], (old: Employee[] | undefined) => {
        return old
          ? old.map((employee) => (employee.cpf === cpf ? data : employee))
          : [];
      });
      setOpen(false);
      toast.success("Employee updated");
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });

  const form = useForm({
    defaultValues: {
      name: name,
      phone: phone,
      email: email,
      isManager: isManager,
      password: "",
    },
    onSubmit: ({ value }) => {
      const { name, phone, email, password } = value;
      mutation.mutate({
        name,
        phone,
        email,
        isManager,
        passwordHash: password,
      });
    },
  });

  // const current;

  function formatPhone(value: string) {
    const cleanedValue = value.replace(/\D/g, "").slice(0, 11);

    return cleanedValue
      .replace(/(\d{2})(\d)/, "($1) $2")
      .replace(/(\d{5})(\d{4})/, "$1-$2");
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button
          variant="ghost"
          size="sm"
          className="w-full text-left justify-start text-sm"
        >
          Edit
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>Edit employee</DialogTitle>
        </DialogHeader>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            e.stopPropagation();
            form.handleSubmit();
          }}
        >
          <div className="grid gap-4 py-4">
            <form.Field
              name="name"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Name
                    </Label>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                      type="text"
                      placeholder="Employee name"
                      className="col-span-3"
                    />
                  </div>
                );
              }}
            />
            <form.Field
              name="phone"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Phone
                    </Label>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) =>
                        field.handleChange(formatPhone(e.target.value))
                      }
                      type="text"
                      placeholder="Employee phone"
                      className="col-span-3"
                    />
                  </div>
                );
              }}
            />
            <form.Field
              name="email"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Email
                    </Label>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                      type="email"
                      placeholder="Employee email"
                      className="col-span-3"
                    />
                  </div>
                );
              }}
            />
            <form.Field
              name="isManager"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Manager
                    </Label>
                    <Switch
                      id={field.name}
                      name={field.name}
                      checked={field.state.value}
                      onCheckedChange={(e) => field.handleChange(e)}
                      className="col-span-3"
                    />
                  </div>
                );
              }}
            />
            <form.Field
              name="password"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Password
                    </Label>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                      type="password"
                      placeholder="Employee password"
                      className="col-span-3"
                    />
                  </div>
                );
              }}
            />
          </div>
          <DialogFooter>
            <Button disabled={mutation.isPending} type="submit">
              Save changes
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};
