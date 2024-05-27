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
import { Employee } from "@/types/auth";

export const DeleteEmployeeModal = ({ cpf }: { cpf: string }) => {
  const api = useAxios();
  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: () => {
      return api.delete(`/employee/${cpf}`);
    },
    onSuccess: () => {
      toast.success("Employee deleted");
      queryClient.setQueryData(["employees"], (old: Employee[] | undefined) => {
        return old ? old.filter((employee) => employee.cpf !== cpf) : [];
      });
    },
    onError: (error) => {
      toast.error(error.message);
    },
  });

  const me = queryClient.getQueryData<Employee>(["currentUser"]);

  if (me?.cpf === cpf) {
    return null;
  }

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
            employee.
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
