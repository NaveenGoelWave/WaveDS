import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { toApiError } from "../../../api/client";
import { formatDate } from "../../../utils/date";
import { useCustomers, useDeleteCustomer, useFormOptions } from "../api/customerApi";

const PAGE_SIZE = 10;

export default function CustomerListPage() {
  const [searchInput, setSearchInput] = useState("");
  const [search, setSearch] = useState("");
  const [zone, setZone] = useState("");
  const [brand, setBrand] = useState("");
  const [page, setPage] = useState(0);

  // Wait 300ms after typing stops before querying the server.
  useEffect(() => {
    const t = setTimeout(() => {
      setSearch(searchInput.trim());
      setPage(0);
    }, 300);
    return () => clearTimeout(t);
  }, [searchInput]);

  const options = useFormOptions();
  const { data, isLoading, isError, error, isFetching } = useCustomers({
    search,
    zone,
    brand,
    page,
    size: PAGE_SIZE,
  });
  const remove = useDeleteCustomer();

  const onDelete = (id: number, name: string) => {
    if (window.confirm(`Delete customer "${name}"? This cannot be undone.`)) {
      remove.mutate(id);
    }
  };

  return (
    <div>
      <div className="page-header">
        <h1>Customers</h1>
        <Link to="/customers/new" className="btn primary">
          Add customer
        </Link>
      </div>

      <div className="filters">
        <input
          placeholder="Search name, phone, email, city, pincode..."
          value={searchInput}
          onChange={(e) => setSearchInput(e.target.value)}
        />
        <select
          value={zone}
          onChange={(e) => {
            setZone(e.target.value);
            setPage(0);
          }}
        >
          <option value="">All zones</option>
          {options.data?.zones.map((z) => (
            <option key={z.value} value={z.value}>
              {z.label}
            </option>
          ))}
        </select>
        <select
          value={brand}
          onChange={(e) => {
            setBrand(e.target.value);
            setPage(0);
          }}
        >
          <option value="">All brands</option>
          {options.data?.brands.map((b) => (
            <option key={b.value} value={b.value}>
              {b.label}
            </option>
          ))}
        </select>
      </div>

      {remove.isError && <div className="alert">{toApiError(remove.error).message}</div>}
      {isError && <div className="alert">{toApiError(error).message}. Is the backend running?</div>}
      {isLoading && <p>Loading...</p>}

      {data && (
        <>
          <div className={`table-wrap${isFetching ? " fetching" : ""}`}>
            <table>
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Contact</th>
                  <th>City</th>
                  <th>Area/Sector</th>
                  <th>Zone</th>
                  <th>Brand</th>
                  <th>Customer type</th>
                  <th>Company</th>
                  <th>Start date</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {data.content.length === 0 && (
                  <tr>
                    <td colSpan={10} className="empty">
                      No customers found.
                    </td>
                  </tr>
                )}
                {data.content.map((c) => (
                  <tr key={c.id}>
                    <td>{c.customerName}</td>
                    <td>{c.contactNumber}</td>
                    <td>{c.city}</td>
                    <td>{c.areaSector}</td>
                    <td>{c.zoneLabel}</td>
                    <td>{c.brandLabel}</td>
                    <td>{c.customerType}</td>
                    <td>{c.company}</td>
                    <td>{formatDate(c.startDate)}</td>
                    <td className="row-actions">
                      <Link to={`/customers/${c.id}/edit`}>Edit</Link>
                      <button className="link danger" onClick={() => onDelete(c.id, c.customerName)}>
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className="pager">
            <span>
              {data.totalElements} customer{data.totalElements === 1 ? "" : "s"}
            </span>
            <div>
              <button className="btn secondary" disabled={page === 0} onClick={() => setPage(page - 1)}>
                Previous
              </button>
              <span className="page-num">
                Page {data.totalPages === 0 ? 0 : page + 1} of {data.totalPages}
              </span>
              <button
                className="btn secondary"
                disabled={page + 1 >= data.totalPages}
                onClick={() => setPage(page + 1)}
              >
                Next
              </button>
            </div>
          </div>
        </>
      )}
    </div>
  );
}
