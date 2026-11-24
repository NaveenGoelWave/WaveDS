import type { ReactNode } from "react";

interface FieldProps {
  label: string;
  required?: boolean;
  error?: string;
  wide?: boolean;
  children: ReactNode;
}

export default function Field({ label, required, error, wide, children }: FieldProps) {
  return (
    <label className={`field${wide ? " wide" : ""}`}>
      <span className="field-label">
        {label}
        {required && <span className="req"> *</span>}
      </span>
      {children}
      {error && <span className="field-error">{error}</span>}
    </label>
  );
}
