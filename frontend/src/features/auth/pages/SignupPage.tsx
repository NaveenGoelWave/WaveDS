import { useEffect, useState } from "react";
import { Link, Navigate, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toApiError, type ApiError } from "../../../api/client";
import Field from "../../customers/components/Field";
import { useAuth } from "../context/AuthContext";
import { signupSchema, type SignupFormValues } from "../schemas/authSchemas";

export default function SignupPage() {
  const { isAuthenticated, signup } = useAuth();
  const navigate = useNavigate();

  const [serverError, setServerError] = useState<ApiError | null>(null);
  const {
    register,
    handleSubmit,
    setError,
    formState: { errors, isSubmitting },
  } = useForm<SignupFormValues>({
    resolver: zodResolver(signupSchema),
    defaultValues: { fullName: "", email: "", password: "", confirmPassword: "" },
  });

  // Show field errors the server reports (for example "email already exists") next to the field.
  useEffect(() => {
    const fieldErrors = serverError?.fieldErrors;
    if (!fieldErrors) return;
    for (const [field, message] of Object.entries(fieldErrors)) {
      if (field === "fullName" || field === "email" || field === "password") {
        setError(field, { type: "server", message });
      }
    }
  }, [serverError, setError]);

  if (isAuthenticated) return <Navigate to="/customers" replace />;

  const onSubmit = async (values: SignupFormValues) => {
    setServerError(null);
    try {
      await signup({ fullName: values.fullName.trim(), email: values.email.trim(), password: values.password });
      navigate("/customers", { replace: true });
    } catch (e) {
      setServerError(toApiError(e));
    }
  };

  return (
    <div className="auth-wrap">
      <form className="auth-card" onSubmit={handleSubmit(onSubmit)} noValidate>
        <div className="auth-brand">WaveDS</div>
        <h1>Create account</h1>
        {serverError && !serverError.fieldErrors && <div className="alert">{serverError.message}</div>}
        <div className="auth-fields">
          <Field label="Full name" required error={errors.fullName?.message}>
            <input autoComplete="name" autoFocus {...register("fullName")} />
          </Field>
          <Field label="Email" required error={errors.email?.message}>
            <input type="email" autoComplete="username" {...register("email")} />
          </Field>
          <Field label="Password" required error={errors.password?.message}>
            <input type="password" autoComplete="new-password" {...register("password")} />
          </Field>
          <Field label="Confirm password" required error={errors.confirmPassword?.message}>
            <input type="password" autoComplete="new-password" {...register("confirmPassword")} />
          </Field>
        </div>
        <p className="hint">At least 8 characters, with a letter and a number.</p>
        <button type="submit" className="btn primary block" disabled={isSubmitting}>
          {isSubmitting ? "Creating account..." : "Create account"}
        </button>
        <p className="auth-switch">
          Already have an account? <Link to="/login">Sign in</Link>
        </p>
      </form>
    </div>
  );
}
