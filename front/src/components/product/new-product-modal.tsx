import { useState } from "react";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useForm } from "@tanstack/react-form";
import { ChevronsUpDown, PlusCircle } from "lucide-react";

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
import { Category, Product } from "@/types/storage";
import { Badge } from "../ui/badge";
import { categoriesFetch } from "@/api/queries";
import {
  DropdownMenu,
  DropdownMenuCheckboxItem,
  DropdownMenuContent,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu";
import { toast } from "sonner";

export const NewProductModal = () => {
  const [open, setOpen] = useState(false);
  const [categoryArray, setCategoryArray] = useState<string[]>([]);

  const api = useAxios();

  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: (product: Product): Promise<Product> => {
      return api
        .post("/product", {
          name: product.name,
          weight: product.weight,
          volume: product.volume,
          categories: product.categories,
        })
        .then((res) => res.data);
    },
    onSuccess: (data: Product) => {
      toast.success("Product created");
      queryClient.setQueryData(["products"], (old: Product[] | undefined) => {
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
      name: "",
      weight: "",
      volume: "",
      categories: [],
    },
    onSubmit: ({ value }) => {
      const { name, weight, volume } = value;
      const numberWeight = parseFloat(weight);
      const numberVolume = parseFloat(volume);
      mutation.mutate({
        name,
        weight: numberWeight,
        volume: numberVolume,
        categories: categoryArray,
      });
    },
  });

  const { isPending, data, error } = useQuery({
    queryKey: ["categories"],
    queryFn: () => categoriesFetch(api),
  });

  if (isPending) return <div>Loading...</div>;

  if (error) {
    return <div>Error</div>;
  }

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="outline" className="mr-4">
          <PlusCircle className="h-4 w-4" />
          <span className="ml-2">New product</span>
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>New product</DialogTitle>
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
                      placeholder="Product name"
                      className="col-span-3"
                    />
                  </div>
                );
              }}
            />
            <form.Field
              name="weight"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Weight (kg)
                    </Label>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                      type="text"
                      placeholder="Product weight"
                      className="col-span-3"
                    />
                  </div>
                );
              }}
            />
            <form.Field
              name="volume"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Volume (L)
                    </Label>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                      type="text"
                      placeholder="Product weight"
                      className="col-span-3"
                    />
                  </div>
                );
              }}
            />
            <div className="grid grid-cols-4 items-center gap-4">
              <Label htmlFor="categories" className="text-right">
                Categories
              </Label>
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button
                    variant="outline"
                    role="combobox"
                    aria-expanded={open}
                    className="col-span-3 justify-between"
                  >
                    {categoryArray !== undefined && categoryArray.length > 0
                      ? categoryArray.map((category, index) => (
                          <Badge
                            key={index}
                            variant="secondary"
                            className="mr-1"
                          >
                            {category}
                          </Badge>
                        ))
                      : "Select categories..."}
                    <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent className="w-80">
                  <DropdownMenuLabel>Categories</DropdownMenuLabel>
                  <DropdownMenuSeparator />
                  {data.map((category: Category) => (
                    <DropdownMenuCheckboxItem
                      checked={categoryArray.includes(category.name)}
                      onCheckedChange={(checked) => {
                        if (checked) {
                          setCategoryArray([...categoryArray, category.name]);
                        } else {
                          setCategoryArray(
                            categoryArray.filter(
                              (item) => item !== category.name
                            )
                          );
                        }
                      }}
                      key={category.id}
                    >
                      {category.name}
                    </DropdownMenuCheckboxItem>
                  ))}
                </DropdownMenuContent>
              </DropdownMenu>
            </div>
          </div>
          <DialogFooter>
            <Button type="submit">Create</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};
