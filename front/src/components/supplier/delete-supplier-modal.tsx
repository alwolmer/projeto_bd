import { useAxios } from "@/lib/use-axios";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { Button } from "../ui/button";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { Supplier } from "@/types/storage";

export const DeleteSupplierModal = ({ cnpj }: { cnpj: string }) => {
  const api = useAxios();
  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: () => {
      return api.delete(
        `/supplier/${cnpj.replace(".", "").replace("/", "").replace("-", "")}`
      );
    },
    onSuccess: () => {
      toast.success("Supplier deleted");
      queryClient.setQueryData(["suppliers"], (old: Supplier[] | undefined) => {
        return old ? old.filter((supplier) => supplier.cnpj !== cnpj) : [];
      });
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });

  return (
    <AlertDialog>
      <AlertDialogTrigger asChild>
        <Button
          variant="ghost"
          size="sm"
          className="w-full text-left justify-start text-sm"
        >
          Delete
        </Button>
      </AlertDialogTrigger>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
          <AlertDialogDescription>
            This action cannot be undone. This will permanently delete the
            supplier.
          </AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <AlertDialogAction onClick={() => mutation.mutate()}>
            Delete
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  );
};
