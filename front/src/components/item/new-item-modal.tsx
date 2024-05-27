import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
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
import { toast } from "sonner";
import { Item, Product } from "@/types/storage";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { productsFetch, suppliersFetch } from "@/api/queries";

export const NewItemModal = () => {
  const [open, setOpen] = useState(false);

  const api = useAxios();

  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: (item: Item): Promise<Item> => {
      return api
        .post("/item", {
          productId: item.productId,
          supplierCnpj: item.supplierCnpj,
        })
        .then((res) => res.data);
    },
    onSuccess: (data: Item) => {
      toast.success("Item created");
      queryClient.setQueryData(["items"], (old: Item[] | undefined) => {
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
      productId: "",
      supplierCnpj: "",
    },
    onSubmit: ({ value }) => {
      const { productId, supplierCnpj } = value;
      mutation.mutate({
        productId,
        supplierCnpj,
      });
    },
  });

  const {
    isPending: isProductsPending,
    data: productData,
    error: productError,
  } = useQuery({
    queryKey: ["products"],
    queryFn: () => productsFetch(api),
  });

  const {
    isPending: isSuppliersPending,
    data: supplierData,
    error: supplierError,
  } = useQuery({
    queryKey: ["suppliers"],
    queryFn: () => suppliersFetch(api),
  });

  if (isProductsPending || isSuppliersPending) return <div>Loading...</div>;

  if (productError || supplierError) {
    return <div>Error</div>;
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="outline" className="mr-4">
          <PlusCircle className="h-4 w-4" />
          <span className="ml-2">New Item</span>
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>New Item</DialogTitle>
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
              name="productId"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Product
                    </Label>
                    <Select
                      onValueChange={field.handleChange}
                      defaultValue={field.state.value}
                    >
                      <SelectTrigger className="col-span-3">
                        <SelectValue placeholder="Select a product" />
                      </SelectTrigger>
                      <SelectContent>
                        {productData?.map((product: Product) => (
                          <SelectItem key={product.id!} value={product.id!}>
                            {product.name}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                );
              }}
            />
            <form.Field
              name="supplierCnpj"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Supplier
                    </Label>
                    <Select
                      onValueChange={field.handleChange}
                      defaultValue={field.state.value}
                    >
                      <SelectTrigger className="col-span-3">
                        <SelectValue placeholder="Select a product" />
                      </SelectTrigger>
                      <SelectContent>
                        {supplierData?.map((supplier) => (
                          <SelectItem
                            key={supplier.cnpj}
                            value={supplier.cnpj!}
                          >
                            {supplier.name}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
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
