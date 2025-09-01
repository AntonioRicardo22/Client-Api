-- Initialize the database with seed data
-- This script runs when the PostgreSQL container starts for the first time

-- Create extensions if needed
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS address (
    id SERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    zip_code VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS clients (
    id SERIAL PRIMARY KEY,
    cpf VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    address_id INT NOT NULL,
    date_register TIMESTAMP NOT NULL,
    date_update TIMESTAMP NOT NULL,
    FOREIGN KEY (address_id) REFERENCES address(id)
);

-- Insert sample addresses
INSERT INTO address (id, street, city, state, zip_code) VALUES
(1, 'Rua das Flores, 123', 'São Paulo', 'SP', '01234567'),
(2, 'Avenida Paulista, 1000', 'São Paulo', 'SP', '01310100'),
(3, 'Rua da Consolação, 456', 'São Paulo', 'SP', '01302000'),
(4, 'Avenida Atlântica, 789', 'Rio de Janeiro', 'RJ', '22070010'),
(5, 'Rua Augusta, 321', 'São Paulo', 'SP', '01305000'),
(6, 'Avenida Beira Mar, 654', 'Fortaleza', 'CE', '60060000'),
(7, 'Rua da Liberdade, 987', 'São Paulo', 'SP', '01503000'),
(8, 'Avenida Boa Viagem, 147', 'Recife', 'PE', '51030000'),
(9, 'Rua Oscar Freire, 258', 'São Paulo', 'SP', '01426000'),
(10, 'Avenida Paulista, 2000', 'São Paulo', 'SP', '01310100')
ON CONFLICT (id) DO NOTHING;

-- Insert sample clients
INSERT INTO clients (id, cpf, name, birth_date, email, phone_number, address_id, date_register, date_update) VALUES
(1, '12345678901', 'João Silva', '1990-05-15', 'joao.silva@email.com', '11987654321', 1, NOW(), NOW()),
(2, '98765432100', 'Maria Santos', '1985-08-22', 'maria.santos@email.com', '11987654322', 2, NOW(), NOW()),
(3, '11122233344', 'Pedro Oliveira', '1992-12-03', 'pedro.oliveira@email.com', '11987654323', 3, NOW(), NOW()),
(4, '55566677788', 'Ana Costa', '1988-03-18', 'ana.costa@email.com', '11987654324', 4, NOW(), NOW()),
(5, '99988877766', 'Carlos Ferreira', '1995-07-25', 'carlos.ferreira@email.com', '11987654325', 5, NOW(), NOW()),
(6, '44433322211', 'Lucia Almeida', '1987-11-12', 'lucia.almeida@email.com', '11987654326', 6, NOW(), NOW()),
(7, '77788899900', 'Roberto Lima', '1991-09-08', 'roberto.lima@email.com', '11987654327', 7, NOW(), NOW()),
(8, '22233344455', 'Fernanda Rocha', '1993-04-30', 'fernanda.rocha@email.com', '11987654328', 8, NOW(), NOW()),
(9, '66677788899', 'Marcos Pereira', '1989-01-14', 'marcos.pereira@email.com', '11987654329', 9, NOW(), NOW()),
(10, '33344455566', 'Juliana Souza', '1994-06-20', 'juliana.souza@email.com', '11987654330', 10, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

-- Update sequence values to avoid conflicts
SELECT setval('address_id_seq', (SELECT MAX(id) FROM address));
SELECT setval('clients_id_seq', (SELECT MAX(id) FROM clients));

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_clients_cpf ON clients(cpf);
CREATE INDEX IF NOT EXISTS idx_clients_email ON clients(email);
CREATE INDEX IF NOT EXISTS idx_clients_name ON clients(name);
CREATE INDEX IF NOT EXISTS idx_address_city ON address(city);
CREATE INDEX IF NOT EXISTS idx_address_state ON address(state);
