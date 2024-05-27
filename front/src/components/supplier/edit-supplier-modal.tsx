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

export const EditSupplierModal = ({ cnpj, name, phone, email }: Supplier) => {
  const [open, setOpen] = useState(false);

  const api = useAxios();

  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: (supplier: Supplier): Promise<Supplier> => {
      return api
        .patch(
          `/supplier/${cnpj?.replace(".", "").replace("/", "").replace("-", "")}`,
          {
            name: supplier.name,
            phone: supplier.phone,
            email: supplier.email,
          }
        )
        .then((res) => res.data);
    },
    onSuccess: (data: Supplier) => {
      queryClient.setQueryData(["suppliers"], (old: Supplier[] | undefined) => {
        return old
          ? old.map((supplier) => (supplier.cnpj === cnpj ? data : supplier))
          : [];
      });
      setOpen(false);
      toast.success("Supplier updated");
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
    },
    onSubmit: ({ value }) => {
      const { name, phone, email } = value;
      mutation.mutate({
        name,
        phone,
        email,
      });
    },
  });

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
          <DialogTitle>Edit supplier</DialogTitle>
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
                      placeholder="Supplier name"
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
                      placeholder="Supplier phone"
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
                      placeholder="Supplier email"
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
