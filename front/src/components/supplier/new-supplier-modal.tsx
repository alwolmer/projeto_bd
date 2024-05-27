import { useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useForm } from "@tanstack/react-form";
import { PlusCircle } from "lucide-react";

import { useAxios } from "@/lib/use-axios";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { toast } from "sonner";
import { Supplier } from "@/types/storage";

export const NewSupplierModal = () => {
  const [open, setOpen] = useState(false);

  const api = useAxios();

  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: (supplier: Supplier): Promise<Supplier> => {
      return api
        .post("/supplier", {
          cnpj: supplier.cnpj,
          name: supplier.name,
          phone: supplier.phone,
          email: supplier.email,
        })
        .then((res) => res.data);
    },
    onSuccess: (data: Supplier) => {
      toast.success("Supplier created");
      queryClient.setQueryData(["suppliers"], (old: Supplier[] | undefined) => {
        return old ? [...old, data] : [data];
      });
      setOpen(false);
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });

  const form = useForm({
    defaultValues: {
      cnpj: "",
      name: "",
      phone: "",
      email: "",
    },
    onSubmit: ({ value }) => {
      const { cnpj, name, phone, email } = value;
      mutation.mutate({
        cnpj,
        name,
        phone,
        email,
      });
    },
  });

  function formatCnpj(value: string) {
    const cleanedValue = value.replace(/\D/g, "");

    return cleanedValue
      .replace(/(\d{2})(\d)/, "$1.$2")
      .replace(/(\d{3})(\d)/, "$1.$2")
      .replace(/(\d{3})(\d)/, "$1/$2")
      .replace(/(\d{4})(\d{1,2})/, "$1-$2")
      .replace(/(-\d{2})\d+?$/, "$1");
  }

  function formatPhone(value: string) {
    const cleanedValue = value.replace(/\D/g, "").slice(0, 11);

    return cleanedValue
      .replace(/(\d{2})(\d)/, "($1) $2")
      .replace(/(\d{5})(\d{4})/, "$1-$2");
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="outline" className="mr-4">
          <PlusCircle className="h-4 w-4" />
          <span className="ml-2">New supplier</span>
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>New supplier</DialogTitle>
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
              name="cnpj"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      CNPJ
                    </Label>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) =>
                        field.handleChange(formatCnpj(e.target.value))
                      }
                      type="text"
                      placeholder="Supplier cnpj"
                      className="col-span-3"
                    />
                  </div>
                );
              }}
            />
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
            <Button type="submit">Create</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};
