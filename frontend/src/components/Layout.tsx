import { NavLink, Outlet } from "react-router-dom";

export default function Layout() {
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
      </header>
      <main className="content">
        <Outlet />
      </main>
    </div>
  );
}
