-- Starter dropdown values. Replace/add real ones via POST /api/v1/lookups or a new migration.
INSERT INTO lookup_value (category, label, sort_order) VALUES
    ('CUSTOMER_TYPE', 'Residential',   1),
    ('CUSTOMER_TYPE', 'Commercial',    2),
    ('CUSTOMER_TYPE', 'Corporate',     3),
    ('CUSTOMER_TYPE', 'Government',    4),
    ('CUSTOMER_TYPE', 'Institutional', 5);

-- PLACEHOLDER companies: replace with your real company list.
INSERT INTO lookup_value (category, label, sort_order) VALUES
    ('COMPANY', 'Sample Company A', 1),
    ('COMPANY', 'Sample Company B', 2);
