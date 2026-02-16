/***************************************************** ENUMS *************************************************************/

CREATE TYPE account_level AS enum('client', 'agent', 'support', 'admin');

CREATE TYPE status AS enum('active', 'ended_succesfully', 'removed');

CREATE TYPE listing_type AS enum('rent', 'sale');

/***************************************************** TABLES *************************************************************/

CREATE TABLE account
(
    email VARCHAR(255) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    hash_password VARCHAR(255) NOT NULL,
    account_level account_level NOT NULL,

    CONSTRAINT valid_email CHECK
    (
        email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'
    )
);

CREATE TABLE listing
(
    id SERIAL PRIMARY KEY,
    status status NOT NULL DEFAULT 'active',
    n_views INT NOT NULL DEFAULT 0,
    agent_email VARCHAR(255) REFERENCES account(email) ON UPDATE CASCADE
);

CREATE TABLE offer
(
    id SERIAL PRIMARY KEY,
    proposed_price NUMERIC NOT NULL,
    expiration_date DATE NOT NULL,
    notes VARCHAR(255) NOT NULL,
    status status NOT NULL DEFAULT 'active',
    listing_id INT REFERENCES listing(id) NOT NULL,
    client_email VARCHAR(255) REFERENCES account(email) ON UPDATE CASCADE,
    agent_email VARCHAR(255) REFERENCES account(email) ON UPDATE CASCADE

    CONSTRAINT valid_expiration_date CHECK
    (
        expiration_date > CURRENT_DATE
    )
);

CREATE TABLE visit_request
(
    id SERIAL PRIMARY KEY,
    visit_date TIMESTAMPTZ NOT NULL,
    status status NOT NULL DEFAULT 'active',
    listing_id INT REFERENCES listing(id) NOT NULL,
    client_email VARCHAR(255) REFERENCES account(email) ON UPDATE CASCADE,
    agent_email VARCHAR(255) REFERENCES account(email) ON UPDATE CASCADE

    CONSTRAINT valid_visit_date CHECK
    (
        visit_date > CURRENT_TIMESTAMP
    )
);

CREATE TABLE photo
(
    filepath VARCHAR(255) PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    position INT NOT NULL,
    listing_id INT REFERENCES listing(id),
    UNIQUE (listing_id, position)
);

CREATE TABLE address
(
    id SERIAL PRIMARY KEY,
    city VARCHAR(50) NOT NULL,
    street VARCHAR(100) NOT NULL,
    house_number VARCHAR(10) NOT NULL,
    province VARCHAR(50) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    country VARCHAR(100) NOT NULL, 
    latitude NUMERIC NOT NULL,
    longitude NUMERIC NOT NULL,
    place_id VARCHAR(255),
    formatted_address VARCHAR(255)
);

CREATE TABLE house_info
(
    listing_id INT REFERENCES listing(id) PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    address_id INT REFERENCES address(id) NOT NULL,
    intern INT NOT NULL,
    floor INT NOT NULL,
    elevator BOOLEAN NOT NULL,
    square_meters INT NOT NULL,
    n_rooms INT NOT NULL,
    energy_class VARCHAR(5) NOT NULL,
    other_services VARCHAR(255) NOT NULL
);

CREATE TABLE commercial_info
(
    listing_id INT REFERENCES listing(id) PRIMARY KEY,
    price NUMERIC NOT NULL,
    listing_type listing_type NOT NULL
);

CREATE TABLE surrounding_info
(
    listing_id INT REFERENCES listing(id) PRIMARY KEY,
    near_stops BOOLEAN NOT NULL,
    near_parks BOOLEAN NOT NULL,
    near_schools BOOLEAN NOT NULL
);

CREATE TABLE onboarding_token
(
    token VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) REFERENCES account(email) ON UPDATE CASCADE,
    expiration_date TIMESTAMPTZ NOT NULL,
    is_used BOOLEAN NOT NULL DEFAULT FALSE
);

