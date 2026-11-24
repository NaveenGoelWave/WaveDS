import { NavLink, Outlet } from "react-router-dom";
import { useAuth } from "../features/auth/context/AuthContext";

export default function Layout() {
  const { user, logout } = useAuth();

  return (
    <div className="app">
      <header className="topbar">
        <span className="brand">WaveDS</span>
        <nav>
          <NavLink to="/customers" end>
            Customers
          </NavLink>
          <NavLink to="/customers/new">Add customer</NavLink>
        </nav>
        <div className="topbar-user">
          <span>{user?.fullName}</span>
          <button className="btn topbar-btn" onClick={logout}>
            Sign out
          </button>
        </div>
      </header>
      <main className="content">
        <Outlet />
      </main>
    </div>
  );
}
