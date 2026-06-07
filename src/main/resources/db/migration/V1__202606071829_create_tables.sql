CREATE TABLE users (
  id UUID PRIMARY KEY,
  provider_id VARCHAR(255) NOT NULL,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  picture_url VARCHAR(2048),
  role VARCHAR(50) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE,
  edited_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE events (
  id UUID PRIMARY KEY,
  host_id UUID NOT NULL,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(10000),
  banner_url VARCHAR(2048),
  age_range INTEGER NOT NULL,
  sales_open BOOLEAN NOT NULL,
  postal_code VARCHAR(255),
  country VARCHAR(255) NOT NULL,
  state VARCHAR(255),
  city VARCHAR(255) NOT NULL,
  address_line1 VARCHAR(255) NOT NULL,
  address_line2 VARCHAR(255),
  status VARCHAR(50) NOT NULL,
  start_date TIMESTAMP WITH TIME ZONE NOT NULL,
  end_date TIMESTAMP WITH TIME ZONE NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE,
  edited_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE tickets (
  id UUID PRIMARY KEY,
  event_id UUID NOT NULL,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(2000),
  price NUMERIC(10, 2) NOT NULL,
  currency VARCHAR(3) NOT NULL,
  capacity INTEGER NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE,
  edited_at TIMESTAMP WITH TIME ZONE
);

CREATE TABLE registrations (
  id UUID PRIMARY KEY,
  registrant_id UUID NOT NULL,
  ticket_id UUID NOT NULL,
  event_id UUID NOT NULL,
  holder_name VARCHAR(100) NOT NULL,
  payment_status VARCHAR(50) NOT NULL,
  checkout_url VARCHAR(2048),
  stripe_session_id VARCHAR(255),
  total_paid NUMERIC(10, 2),
  paid_currency VARCHAR(3),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE,
  edited_at TIMESTAMP WITH TIME ZONE
);
