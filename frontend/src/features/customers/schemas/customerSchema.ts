import { z } from "zod";
import type { Customer, CustomerRequest } from "../types/customer";

const required = (label: string) => z.string().trim().min(1, `${label} is required`);

export const customerSchema = z.object({
  customerName: required("Customer name").max(200, "Maximum 200 characters"),
  email: z.string().trim().email("Enter a valid email address").or(z.literal("")),
  contactNumber: required("Contact number").regex(/^\d{10}$/, "Contact number must be 10 digits"),
  alternateNumber: z
    .string()
    .trim()
    .regex(/^\d{10}$/, "Alternate number must be 10 digits")
    .or(z.literal("")),
  address: required("Address").max(500, "Maximum 500 characters"),
  city: required("City").max(100, "Maximum 100 characters"),
  areaSector: required("Area/Sector").max(150, "Maximum 150 characters"),
  zone: required("Zone"),
  siteType: required("Site type"),
  pincode: required("Pincode").regex(/^\d{6}$/, "Pincode must be 6 digits"),
  customerTypeId: required("Customer type"),
  brand: required("Brand"),
  companyId: required("Company"),
  gemOrderNumber: z.string().trim().max(100, "Maximum 100 characters"),
  oldCustomerId: z.string().trim().max(100, "Maximum 100 characters"),
  startDate: required("Date of start"),
});

export type CustomerFormValues = z.infer<typeof customerSchema>;

const orNull = (value: string) => (value.trim() === "" ? null : value.trim());

export function toRequest(values: CustomerFormValues): CustomerRequest {
  return {
    customerName: values.customerName.trim(),
    email: orNull(values.email),
    contactNumber: values.contactNumber.trim(),
    alternateNumber: orNull(values.alternateNumber),
    address: values.address.trim(),
    city: values.city.trim(),
    areaSector: values.areaSector.trim(),
    zone: values.zone,
    siteType: values.siteType,
    pincode: values.pincode.trim(),
    customerTypeId: Number(values.customerTypeId),
    brand: values.brand,
    companyId: Number(values.companyId),
    gemOrderNumber: orNull(values.gemOrderNumber),
    oldCustomerId: orNull(values.oldCustomerId),
    startDate: values.startDate,
  };
}

export function fromCustomer(c: Customer): CustomerFormValues {
  return {
    customerName: c.customerName,
    email: c.email ?? "",
    contactNumber: c.contactNumber,
    alternateNumber: c.alternateNumber ?? "",
    address: c.address,
    city: c.city,
    areaSector: c.areaSector,
    zone: c.zone,
    siteType: c.siteType,
    pincode: c.pincode,
    customerTypeId: String(c.customerTypeId),
    brand: c.brand,
    companyId: String(c.companyId),
    gemOrderNumber: c.gemOrderNumber ?? "",
    oldCustomerId: c.oldCustomerId ?? "",
    startDate: c.startDate,
  };
}

export function emptyValues(today: string): CustomerFormValues {
  return {
    customerName: "",
    email: "",
    contactNumber: "",
    alternateNumber: "",
    address: "",
    city: "",
    areaSector: "",
    zone: "",
    siteType: "",
    pincode: "",
    customerTypeId: "",
    brand: "",
    companyId: "",
    gemOrderNumber: "",
    oldCustomerId: "",
    startDate: today,
  };
}
