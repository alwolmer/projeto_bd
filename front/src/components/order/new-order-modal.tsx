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
import { toast } from "sonner";
import { Address, Item, Order } from "@/types/storage";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  addressFetch,
  carriersFetch,
  clientsFetch,
  productsFetch,
  stockFetch,
} from "@/api/queries";
import {
  DropdownMenu,
  DropdownMenuCheckboxItem,
  DropdownMenuContent,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu";
import { Badge } from "../ui/badge";

export const NewOrderModal = () => {
  const [open, setOpen] = useState(false);
  const [clientAddress, setClientAddress] = useState<Address[]>([]);
  const [itemArray, setItemArray] = useState<string[]>([]);
  const [itemIdArray, setItemIdArray] = useState<string[]>([]);

  const api = useAxios();

  const queryClient = useQueryClient();

  const mutation = useMutation({
    mutationFn: (order: Order): Promise<Order> => {
      console.log(itemIdArray);
      return api
        .post("/order", {
          clientId: order.clientId,
          deliveryAddressId: order.deliveryAddressId,
          carrierCnpj: order.carrierCnpj,
          itemIds: itemIdArray,
        })
        .then((res) => res.data);
    },
    onSuccess: (data: Order) => {
      toast.success("Order created");
      queryClient.setQueryData(["orders"], (old: Order[] | undefined) => {
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
      clientId: "",
      deliveryAddressId: "",
      carrierCnpj: "",
    },
    onSubmit: ({ value }) => {
      const { clientId, deliveryAddressId, carrierCnpj } = value;

      mutation.mutate({
        clientId,
        deliveryAddressId,
        carrierCnpj,
      });
    },
  });

  const {
    isPending: isPendingClient,
    data: clientData,
    error: clientError,
  } = useQuery({
    queryKey: ["clients"],
    queryFn: () => clientsFetch(api),
  });

  const {
    isPending: isPendingAddress,
    data: addressData,
    error: addressError,
  } = useQuery({
    queryKey: ["addresses"],
    queryFn: () => addressFetch(api),
  });

  const {
    isPending: isPendingCarrier,
    data: carrierData,
    error: carrierError,
  } = useQuery({
    queryKey: ["carriers"],
    queryFn: () => carriersFetch(api),
  });

  const {
    isPending: isStockPending,
    data: stockData,
    error: stockError,
  } = useQuery({
    queryKey: ["stock"],
    queryFn: () => stockFetch(api),
  });

  const {
    isPending: isPendingProduct,
    data: productData,
    error: productError,
  } = useQuery({
    queryKey: ["products"],
    queryFn: () => productsFetch(api),
  });

  if (
    isPendingAddress ||
    isPendingCarrier ||
    isPendingClient ||
    isStockPending ||
    isPendingProduct
  )
    return <div>Loading...</div>;

  if (
    clientError ||
    addressError ||
    carrierError ||
    stockError ||
    productError
  ) {
    return <div>Error</div>;
  }

  const clients = clientData as { id: string; name: string }[];
  const carriers = carrierData as { cnpj: string; name: string }[];

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button variant="outline" className="mr-4">
          <PlusCircle className="h-4 w-4" />
          <span className="ml-2">New Order</span>
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>New Order</DialogTitle>
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
                        setClientAddress(
                          addressData.filter(
                            (address) => address.clientId === value
                          )
                        );
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
            <form.Field
              name="deliveryAddressId"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Delivery Address
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
                        {clientAddress.map((address) => (
                          <SelectItem key={address.id} value={address.id!}>
                            {address.recipientName}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                );
              }}
            />
            <form.Field
              name="carrierCnpj"
              children={(field) => {
                return (
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor={field.name} className="text-right">
                      Carrier
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
                        {carriers.map((carrier) => (
                          <SelectItem key={carrier.cnpj} value={carrier.cnpj}>
                            {carrier.name}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                );
              }}
            />
            <div className="grid grid-cols-4 items-center gap-4">
              <Label className="text-right">Items</Label>
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button
                    variant="outline"
                    role="combobox"
                    aria-expanded={open}
                    className="col-span-3 justify-between"
                  >
                    {itemArray !== undefined && itemArray.length > 0
                      ? itemArray.map((item, index) => (
                          <Badge
                            key={index}
                            variant="secondary"
                            className="mr-1"
                          >
                            {item}
                          </Badge>
                        ))
                      : "Select categories..."}
                    <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent className="w-80">
                  <DropdownMenuLabel>Items</DropdownMenuLabel>
                  <DropdownMenuSeparator />
                  {stockData.map((item: Item) => (
                    <DropdownMenuCheckboxItem
                      checked={itemIdArray.includes(item.id!)}
                      onCheckedChange={(checked) => {
                        if (checked) {
                          setItemIdArray([...itemIdArray, item.id!]);
                          setItemArray([
                            ...itemArray,
                            productData.find(
                              (product) => product.id === item.productId
                            )?.name ?? "",
                          ]);
                        } else {
                          setItemIdArray(
                            itemIdArray.filter((id) => id !== item.id)
                          );
                          setItemArray(
                            itemArray.filter(
                              (name) =>
                                name !==
                                productData.find(
                                  (product) => product.id === item.productId
                                )?.name
                            )
                          );
                        }
                      }}
                      key={item.id}
                    >
                      {
                        productData.find(
                          (product) => product.id === item.productId
                        )?.name
                      }
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
