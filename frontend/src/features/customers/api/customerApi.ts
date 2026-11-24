import { keepPreviousData, useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { api } from "../../../api/client";
import type {
  Customer,
  CustomerListParams,
  CustomerRequest,
  FormOptions,
  PageResponse,
} from "../types/customer";

const KEYS = {
  all: ["customers"] as const,
  list: (p: CustomerListParams) => ["customers", "list", p] as const,
  detail: (id: number) => ["customers", "detail", id] as const,
  options: ["form-options"] as const,
};

export function useFormOptions() {
  return useQuery({
    queryKey: KEYS.options,
    queryFn: async () => (await api.get<FormOptions>("/form-options")).data,
    staleTime: 5 * 60 * 1000,
  });
}

export function useCustomers(params: CustomerListParams) {
  return useQuery({
    queryKey: KEYS.list(params),
    queryFn: async () => {
      const { search, zone, brand, page, size } = params;
      const res = await api.get<PageResponse<Customer>>("/customers", {
        params: {
          page,
          size,
          search: search || undefined,
          zone: zone || undefined,
          brand: brand || undefined,
        },
      });
      return res.data;
    },
    placeholderData: keepPreviousData,
  });
}

export function useCustomer(id: number | undefined) {
  return useQuery({
    queryKey: KEYS.detail(id ?? 0),
    queryFn: async () => (await api.get<Customer>(`/customers/${id}`)).data,
    enabled: id !== undefined,
  });
}

export function useCreateCustomer() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: async (body: CustomerRequest) => (await api.post<Customer>("/customers", body)).data,
    onSuccess: () => qc.invalidateQueries({ queryKey: KEYS.all }),
  });
}

export function useUpdateCustomer(id: number) {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: async (body: CustomerRequest) => (await api.put<Customer>(`/customers/${id}`, body)).data,
    onSuccess: () => qc.invalidateQueries({ queryKey: KEYS.all }),
  });
}

export function useDeleteCustomer() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: async (id: number) => {
      await api.delete(`/customers/${id}`);
    },
    onSuccess: () => qc.invalidateQueries({ queryKey: KEYS.all }),
  });
}
