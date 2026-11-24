import { useState } from "react";
import { Link, Navigate, useLocation, useNavigate } from "react-router-dom";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toApiError } from "../../../api/client";
import Field from "../../customers/components/Field";
import { useAuth } from "../context/AuthContext";
import { loginSchema, type LoginFormValues } from "../schemas/authSchemas";

export default function LoginPage() {
  const { isAuthenticated, login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const from = (location.state as { from?: string } | null)?.from ?? "/customers";

  const [error, setError] = useState<string | null>(null);
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginFormValues>({ resolver: zodResolver(loginSchema), defaultValues: { email: "", password: "" } });

  if (isAuthenticated) return <Navigate to={from} replace />;

  const onSubmit = async (values: LoginFormValues) => {
    setError(null);
    try {
      await login({ email: values.email.trim(), password: values.password });
      navigate(from, { replace: true });
    } catch (e) {
      setError(toApiError(e).message);
    }
  };

  return (
    <div className="auth-wrap">
      <form className="auth-card" onSubmit={handleSubmit(onSubmit)} noValidate>
        <div className="auth-brand">WaveDS</div>
        <h1>Sign in</h1>
        {error && <div className="alert">{error}</div>}
        <div className="auth-fields">
          <Field label="Email" required error={errors.email?.message}>
            <input type="email" autoComplete="username" autoFocus {...register("email")} />
          </Field>
          <Field label="Password" required error={errors.password?.message}>
            <input type="password" autoComplete="current-password" {...register("password")} />
          </Field>
        </div>
        <button type="submit" className="btn primary block" disabled={isSubmitting}>
          {isSubmitting ? "Signing in..." : "Sign in"}
        </button>
        <p className="auth-switch">
          New here? <Link to="/signup">Create an account</Link>
        </p>
      </form>
    </div>
  );
}
