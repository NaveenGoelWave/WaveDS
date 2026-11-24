export interface EnumOption {
  value: string;
  label: string;
}

export interface LookupOption {
  id: number;
  label: string;
}

export interface FormOptions {
  zones: EnumOption[];
  siteTypes: EnumOption[];
  brands: EnumOption[];
  customerTypes: LookupOption[];
  companies: LookupOption[];
}

/** Payload sent to POST/PUT /customers. */
export interface CustomerRequest {
  customerName: string;
  email: string | null;
  contactNumber: string;
  alternateNumber: string | null;
  address: string;
  city: string;
  areaSector: string;
  zone: string;
  siteType: string;
  pincode: string;
  customerTypeId: number;
  brand: string;
  companyId: number;
  gemOrderNumber: string | null;
  oldCustomerId: string | null;
  startDate: string;
}

export interface Customer {
  id: number;
  customerName: string;
  email: string | null;
  contactNumber: string;
  alternateNumber: string | null;
  address: string;
  city: string;
  areaSector: string;
  zone: string;
  zoneLabel: string;
  siteType: string;
  siteTypeLabel: string;
  pincode: string;
  customerTypeId: number;
  customerType: string;
  brand: string;
  brandLabel: string;
  companyId: number;
  company: string;
  gemOrderNumber: string | null;
  oldCustomerId: string | null;
  startDate: string;
  createdAt: string;
  updatedAt: string;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface CustomerListParams {
  search: string;
  zone: string;
  brand: string;
  page: number;
  size: number;
}
