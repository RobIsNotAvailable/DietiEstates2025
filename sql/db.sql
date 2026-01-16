/***************************************************** ENUMS *************************************************************/

CREATE TYPE security_level AS enum('agent','support', 'admin');

CREATE TYPE status AS enum('active', 'ended_succesfully', 'removed');

CREATE TYPE listing_type AS enum('rent', 'sale');

/***************************************************** TABLES *************************************************************/

CREATE TABLE client
(
    email VARCHAR(255) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    hash_password VARCHAR(255) NOT NULL

    CONSTRAINT valid_email CHECK
    (
        email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'
    )
);


CREATE TABLE company_account
(
    email VARCHAR(255) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    company_name VARCHAR(255) NOT NULL,
    hash_password VARCHAR(255) NOT NULL,
    security_level security_level NOT NULL,

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
    agent_email VARCHAR(255) REFERENCES company_account(email) ON UPDATE CASCADE
);

CREATE TABLE offer
(
    id SERIAL PRIMARY KEY,
    proposed_price NUMERIC NOT NULL,
    expiration_date DATE NOT NULL,
    notes VARCHAR(255) NOT NULL,
    status status NOT NULL DEFAULT 'active',
    listing_id INT REFERENCES listing(id) NOT NULL,
    client_email VARCHAR(255) REFERENCES client(email) ON UPDATE CASCADE,
    agent_email VARCHAR(255) REFERENCES company_account(email) ON UPDATE CASCADE

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
    client_email VARCHAR(255) REFERENCES client(email) ON UPDATE CASCADE,
    agent_email VARCHAR(255) REFERENCES company_account(email) ON UPDATE CASCADE

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

CREATE TABLE house_info
(
    listing_id INT REFERENCES listing(id) PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    intern INT NOT NULL,
    floor INT NOT NULL,
    has_elevator BOOLEAN NOT NULL,
    square_meters INT NOT NULL,
    n_rooms INT NOT NULL,
    energy_class VARCHAR(5) NOT NULL,
    other_services VARCHAR(255) NOT NULL
);

CREATE TABLE surrounding_info
(
    listing_id INT REFERENCES listing(id) PRIMARY KEY,
    has_stops BOOLEAN NOT NULL,
    has_parks BOOLEAN NOT NULL,
    has_schools BOOLEAN NOT NULL
);

CREATE TABLE commercial_info
(
    listing_id INT REFERENCES listing(id) PRIMARY KEY,
    price NUMERIC NOT NULL,
    listing_type listing_type NOT NULL
)


