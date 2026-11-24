import { useEffect } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import type { ApiError } from "../../../api/client";
import type { FormOptions } from "../types/customer";
import { customerSchema, type CustomerFormValues } from "../schemas/customerSchema";
import Field from "./Field";

interface Props {
  options: FormOptions;
  defaultValues: CustomerFormValues;
  submitLabel: string;
  submitting: boolean;
  serverError?: ApiError | null;
  onSubmit: (values: CustomerFormValues) => void;
  onCancel: () => void;
}

export default function CustomerForm({
  options,
  defaultValues,
  submitLabel,
  submitting,
  serverError,
  onSubmit,
  onCancel,
}: Props) {
  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<CustomerFormValues>({
    resolver: zodResolver(customerSchema),
    defaultValues,
  });

  // Show field-level errors returned by the backend next to the matching inputs.
  useEffect(() => {
    const fieldErrors = serverError?.fieldErrors;
    if (!fieldErrors) return;
    for (const [field, message] of Object.entries(fieldErrors)) {
      if (field in defaultValues) {
        setError(field as keyof CustomerFormValues, { type: "server", message });
      }
    }
  }, [serverError, defaultValues, setError]);

  return (
    <form onSubmit={handleSubmit(onSubmit)} noValidate>
      {serverError && !serverError.fieldErrors && <div className="alert">{serverError.message}</div>}

      <section className="card">
        <h2>Customer details</h2>
        <div className="grid">
          <Field label="Customer Name" required error={errors.customerName?.message}>
            <input {...register("customerName")} autoFocus />
          </Field>
          <Field label="Email" error={errors.email?.message}>
            <input type="email" {...register("email")} />
          </Field>
          <Field label="Contact Number" required error={errors.contactNumber?.message}>
            <input inputMode="numeric" maxLength={10} {...register("contactNumber")} />
          </Field>
          <Field label="Alternate Number" error={errors.alternateNumber?.message}>
            <input inputMode="numeric" maxLength={10} {...register("alternateNumber")} />
          </Field>
        </div>
      </section>

      <section className="card">
        <h2>Address</h2>
        <div className="grid">
          <Field label="Address" required wide error={errors.address?.message}>
            <textarea rows={3} {...register("address")} />
          </Field>
          <Field label="City" required error={errors.city?.message}>
            <input {...register("city")} />
          </Field>
          <Field label="Area/Sector" required error={errors.areaSector?.message}>
            <input {...register("areaSector")} />
          </Field>
          <Field label="Zone" required error={errors.zone?.message}>
            <select {...register("zone")}>
              <option value="">Select zone</option>
              {options.zones.map((o) => (
                <option key={o.value} value={o.value}>
                  {o.label}
                </option>
              ))}
            </select>
          </Field>
          <Field label="Pincode" required error={errors.pincode?.message}>
            <input inputMode="numeric" maxLength={6} {...register("pincode")} />
          </Field>
        </div>
      </section>

      <section className="card">
        <h2>Account</h2>
        <div className="grid">
          <Field label="Site Type" required error={errors.siteType?.message}>
            <select {...register("siteType")}>
              <option value="">Select site type</option>
              {options.siteTypes.map((o) => (
                <option key={o.value} value={o.value}>
                  {o.label}
                </option>
              ))}
            </select>
          </Field>
          <Field label="Customer Type" required error={errors.customerTypeId?.message}>
            <select {...register("customerTypeId")}>
              <option value="">Select customer type</option>
              {options.customerTypes.map((o) => (
                <option key={o.id} value={String(o.id)}>
                  {o.label}
                </option>
              ))}
            </select>
          </Field>
          <Field label="Brand" required error={errors.brand?.message}>
            <select {...register("brand")}>
              <option value="">Select brand</option>
              {options.brands.map((o) => (
                <option key={o.value} value={o.value}>
                  {o.label}
                </option>
              ))}
            </select>
          </Field>
          <Field label="Company" required error={errors.companyId?.message}>
            <select {...register("companyId")}>
              <option value="">Select company</option>
              {options.companies.map((o) => (
                <option key={o.id} value={String(o.id)}>
                  {o.label}
                </option>
              ))}
            </select>
          </Field>
          <Field label="GEM Order" error={errors.gemOrderNumber?.message}>
            <input {...register("gemOrderNumber")} />
          </Field>
          <Field label="Old Customer ID" error={errors.oldCustomerId?.message}>
            <input {...register("oldCustomerId")} />
          </Field>
          <Field label="Date of Start" required error={errors.startDate?.message}>
            <input type="date" {...register("startDate")} />
          </Field>
        </div>
      </section>

      <div className="actions">
        <button type="button" className="btn secondary" onClick={onCancel} disabled={submitting}>
          Cancel
        </button>
        <button type="submit" className="btn primary" disabled={submitting}>
          {submitting ? "Saving..." : submitLabel}
        </button>
      </div>
    </form>
  );
}
