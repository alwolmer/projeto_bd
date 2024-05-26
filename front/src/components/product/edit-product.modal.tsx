import { categoriesFetch } from "@/api/queries";
import { useAxios } from "@/lib/use-axios";
import { Category, Product } from "@/types/storage";
import { useForm } from "@tanstack/react-form";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { Button } from "../ui/button";
import { ChevronsUpDown, Loader2, PlusCircle } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuCheckboxItem,
  DropdownMenuContent,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu";
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
import { Badge } from "../ui/badge";
import { toast } from "sonner";

export const EditProductModal = ({
  id,
  name,
  weight,
  volume,
  categories,
}: Product) => {
  const [open, setOpen] = useState(false);
  const [categoryArray, setCategoryArray] = useState<string[]>(categories);

  const api = useAxios();

  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: (product: Product): Promise<Product> => {
      return api
        .patch(`/product/${id}`, {
          name: product.name,
          weight: product.weight,
          volume: product.volume,
          categories: product.categories,
        })
        .then((res) => res.data);
    },
    onSuccess: (data: Product) => {
      queryClient.setQueryData(["products"], (old: Product[] | undefined) => {
        return old
          ? old.map((product) => (product.id === id ? data : product))
          : [];
      });
      setOpen(false);
      toast.success("Product updated");
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });

  const form = useForm({
    defaultValues: {
      name: name,
      weight: weight.toString(),
      volume: volume.toString(),
      categories: categories,
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
          <DialogTitle>Edit product</DialogTitle>
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
            <Button disabled={mutation.isPending} type="submit">
              Save changes
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};
