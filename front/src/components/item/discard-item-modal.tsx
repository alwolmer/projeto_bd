import { useState } from "react";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useForm } from "@tanstack/react-form";

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
import { Discard } from "@/types/storage";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";

export const DiscardItemModal = ({ itemId }: { itemId: string }) => {
  const [open, setOpen] = useState(false);

  const api = useAxios();

  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: (discard: Discard): Promise<Discard> => {
      return api
        .post("/discard", {
          itemId: itemId,
          productId: discard.itemId,
          discardReason: discard.discardReason,
        })
        .then((res) => res.data);
    },
    onSuccess: (data: Discard) => {
      toast.success("Item discarded successfully!");
      queryClient.setQueryData(["discards"], (old: Discard[] | undefined) => {
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
      discardReason: "",
    },
    onSubmit: ({ value }) => {
      const { discardReason } = value;
      mutation.mutate({
        itemId,
        discardReason,
      });
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
          Discard
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
              name="discardReason"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Discard Reason
                    </Label>
                    <Select
                      onValueChange={field.handleChange}
                      defaultValue={field.state.value}
                    >
                      <SelectTrigger className="col-span-3">
                        <SelectValue placeholder="Select a product" />
                      </SelectTrigger>
                      <SelectContent>
                        <SelectItem value="loss">Loss</SelectItem>
                        <SelectItem value="Bad Conditioning">
                          Bad Conditioning
                        </SelectItem>
                        <SelectItem value="Fabrication/Transport">
                          Fabrication/Transport
                        </SelectItem>
                      </SelectContent>
                    </Select>
                  </div>
                );
              }}
            />
          </div>
          <DialogFooter>
            <Button type="submit">Discard</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};
