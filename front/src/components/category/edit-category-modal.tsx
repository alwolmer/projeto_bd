import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { useAxios } from "@/lib/use-axios";
import { Category } from "@/types/storage";
import { useForm } from "@tanstack/react-form";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Label } from "../ui/label";
import { Input } from "../ui/input";

export const EditCategoryModal = ({
  id,
  name,
}: {
  id: string;
  name: string;
}) => {
  const [open, setOpen] = useState(false);

  const api = useAxios();

  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: (name: string): Promise<Category> => {
      return api.patch(`/category/${id}`, { name }).then((res) => res.data);
    },
    onSuccess: (data: Category) => {
      toast.success("Category updated");
      queryClient.setQueryData(
        ["categories"],
        (old: Category[] | undefined) => {
          return old
            ? old.map((category) => (category.id === id ? data : category))
            : [];
        }
      );
      setOpen(false);
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });

  const form = useForm({
    defaultValues: {
      name: name,
    },
    onSubmit: ({ value }) => {
      const { name } = value;
      mutation.mutate(name);
    },
  });

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
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Edit category</DialogTitle>
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
                      placeholder="Category name"
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
