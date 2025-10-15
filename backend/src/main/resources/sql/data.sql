-- Insert Address first (foreign key dependency)
INSERT INTO addresses (address_id, street, street_number, city, postal_code, country)
VALUES ('a1b2c3d4-e5f6-7890-abcd-ef1234567890', 'Kerkstraat', '123', 'Beveren', '9120', 'Belgium');

-- Insert Client with the specified UUID
INSERT INTO clients (client_id, first_name, last_name, email, phone_number, birth_date, address_id)
VALUES ('b775ec99-08d9-49c8-9cc7-d0e5d57593bb', 'Jan', 'Janssens', 'jan.janssens@example.com', '+32 123 45 67 89', '1990-05-15', 'a1b2c3d4-e5f6-7890-abcd-ef1234567890');