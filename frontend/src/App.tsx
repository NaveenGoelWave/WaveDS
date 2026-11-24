import { Navigate, Route, Routes } from "react-router-dom";
import Layout from "./components/Layout";
import CustomerListPage from "./features/customers/pages/CustomerListPage";
import CustomerFormPage from "./features/customers/pages/CustomerFormPage";

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<Navigate to="/customers" replace />} />
        <Route path="/customers" element={<CustomerListPage />} />
        <Route path="/customers/new" element={<CustomerFormPage />} />
        <Route path="/customers/:id/edit" element={<CustomerFormPage />} />
        <Route path="*" element={<p>Page not found.</p>} />
      </Route>
    </Routes>
  );
}
