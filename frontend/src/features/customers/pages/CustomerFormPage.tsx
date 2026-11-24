import { useMemo } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toApiError } from "../../../api/client";
import { todayLocal } from "../../../utils/date";
import { useCreateCustomer, useCustomer, useFormOptions, useUpdateCustomer } from "../api/customerApi";
import CustomerForm from "../components/CustomerForm";
import { emptyValues, fromCustomer, toRequest, type CustomerFormValues } from "../schemas/customerSchema";

export default function CustomerFormPage() {
  const { id } = useParams();
  const customerId = id ? Number(id) : undefined;
  const isEdit = customerId !== undefined;
  const navigate = useNavigate();

  const options = useFormOptions();
  const customer = useCustomer(customerId);
  const create = useCreateCustomer();
  const update = useUpdateCustomer(customerId ?? 0);

  const defaultValues = useMemo(
    () => (customer.data ? fromCustomer(customer.data) : emptyValues(todayLocal())),
    [customer.data],
  );

  const mutation = isEdit ? update : create;
  const serverError = mutation.error ? toApiError(mutation.error) : null;

  const onSubmit = (values: CustomerFormValues) => {
    mutation.mutate(toRequest(values), { onSuccess: () => navigate("/customers") });
  };

  if (options.isLoading || (isEdit && customer.isLoading)) return <p>Loading...</p>;
  if (options.isError) return <div className="alert">Could not load form options. Is the backend running?</div>;
  if (isEdit && customer.isError) return <div className="alert">Customer not found.</div>;

  return (
    <div>
      <h1>{isEdit ? "Edit customer" : "Customer induction"}</h1>
      <CustomerForm
        // Remount when switching between customers so the form picks up fresh defaults.
        key={customerId ?? "new"}
        options={options.data!}
        defaultValues={defaultValues}
        submitLabel={isEdit ? "Save changes" : "Add customer"}
        submitting={mutation.isPending}
        serverError={serverError}
        onSubmit={onSubmit}
        onCancel={() => navigate("/customers")}
      />
    </div>
  );
}
