import { useEffect, useState } from "react";
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
import { Input } from "@/components/ui/input";
import { toast } from "sonner";
import { Address } from "@/types/storage";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import axios from "axios";
import { clientsFetch } from "@/api/queries";

interface StatesCall {
  id: number;
  sigla: string;
  nome: string;
  regiao: {
    id: number;
    sigla: string;
    nome: string;
  };
}

interface CitiesCall {
  id: number;
  nome: string;
}

export const NewAddressModal = () => {
  const [open, setOpen] = useState(false);
  const [states, setStates] = useState<StatesCall[]>([]);
  const [cities, setCities] = useState<CitiesCall[]>([]);

  const api = useAxios();

  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: (address: Address): Promise<Address> => {
      return api
        .post("/delivery-address", {
          recipientName: address.recipientName,
          state: address.state,
          city: address.city,
          zip: address.zip,
          street: address.street,
          number: address.number,
          details: address.details,
          clientId: address.clientId,
        })
        .then((res) => res.data);
    },
    onSuccess: (data: Address) => {
      toast.success("Address created");
      queryClient.setQueryData(["addresses"], (old: Address[] | undefined) => {
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
      recipientName: "",
      state: "",
      city: "",
      zip: "",
      street: "",
      number: "",
      details: "",
      clientId: "",
    },
    onSubmit: ({ value }) => {
      const {
        recipientName,
        state,
        city,
        zip,
        street,
        number,
        details,
        clientId,
      } = value;

      mutation.mutate({
        recipientName,
        state,
        city,
        zip,
        street,
        number,
        details,
        clientId,
      });
    },
  });

  const { isPending, data, error } = useQuery({
    queryKey: ["clients"],
    queryFn: () => clientsFetch(api),
  });

  useEffect(() => {
    if (open) {
      axios
        .get("https://servicodados.ibge.gov.br/api/v1/localidades/estados")
        .then((res) => setStates(res.data));
    }
  }, [open]);
  function formatCEP(input: string) {
    // Remove todos os caracteres que não são números
    let cep = input.replace(/\D/g, "");

    // Limita a entrada a 8 dígitos
    if (cep.length > 8) {
      cep = cep.substring(0, 8);
    }

    // Adiciona o hífen após o quinto dígito
    if (cep.length > 5) {
      cep = cep.substring(0, 5) + "-" + cep.substring(5);
    }

    return cep;
  }

  if (isPending) return <div>Loading...</div>;

  if (error) {
    return <div>Error</div>;
  }

  const clients = data as { id: string; name: string }[];

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="outline" className="mr-4">
          <PlusCircle className="h-4 w-4" />
          <span className="ml-2">New Address</span>
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>New Address</DialogTitle>
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
              name="recipientName"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Recipient Name
                    </Label>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                      type="text"
                      placeholder="Recipient name"
                      className="col-span-3"
                    />
                  </div>
                );
              }}
            />
            <form.Field
              name="state"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      State
                    </Label>
                    <Select
                      defaultValue={field.state.value}
                      onValueChange={(value) => {
                        field.handleChange(value);
                        axios
                          .get(
                            `https://servicodados.ibge.gov.br/api/v1/localidades/estados/${value}/municipios`
                          )
                          .then((res) => setCities(res.data));
                      }}
                    >
                      <SelectTrigger className="col-span-3">
                        <SelectValue placeholder="Select a state" />
                      </SelectTrigger>
                      <SelectContent>
                        {states.map((state) => (
                          <SelectItem key={state.sigla} value={state.sigla}>
                            {state.nome}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                );
              }}
            />
            <form.Field
              name="city"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      City
                    </Label>
                    <Select
                      defaultValue={field.state.value}
                      onValueChange={(value) => {
                        field.handleChange(value);
                      }}
                    >
                      <SelectTrigger className="col-span-3">
                        <SelectValue placeholder="Select a city" />
                      </SelectTrigger>
                      <SelectContent>
                        {cities.map((city) => (
                          <SelectItem key={city.id} value={city.nome}>
                            {city.nome}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                );
              }}
            />
            <form.Field
              name="zip"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Zip Code
                    </Label>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) =>
                        field.handleChange(formatCEP(e.target.value))
                      }
                      type="text"
                      placeholder="Zip code"
                      className="col-span-3"
                    />
                  </div>
                );
              }}
            />
            <form.Field
              name="street"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Street
                    </Label>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                      type="text"
                      placeholder="Street"
                      className="col-span-3"
                    />
                  </div>
                );
              }}
            />
            <form.Field
              name="number"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Number
                    </Label>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                      type="text"
                      placeholder="Number"
                      className="col-span-3"
                    />
                  </div>
                );
              }}
            />
            <form.Field
              name="details"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Address Details
                    </Label>
                    <Input
                      id={field.name}
                      name={field.name}
                      value={field.state.value}
                      onBlur={field.handleBlur}
                      onChange={(e) => field.handleChange(e.target.value)}
                      type="text"
                      placeholder="Address Details"
                      className="col-span-3"
                    />
                  </div>
                );
              }}
            />
            <form.Field
              name="clientId"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Client
                    </Label>
                    <Select
                      defaultValue={field.state.value}
                      onValueChange={(value) => {
                        field.handleChange(value);
                      }}
                    >
                      <SelectTrigger className="col-span-3">
                        <SelectValue placeholder="Select a client" />
                      </SelectTrigger>
                      <SelectContent>
                        {clients.map((client) => (
                          <SelectItem key={client.id} value={client.id}>
                            {client.name}
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
