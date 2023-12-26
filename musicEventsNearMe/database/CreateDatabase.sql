DROP DATABASE music_events_near_me;
CREATE DATABASE music_events_near_me;

USE music_events_near_me;
-- Table for MusicEvent
CREATE TABLE music_events (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    identifier VARCHAR(255),
    url VARCHAR(255),
    image VARCHAR(255),
    date_published VARCHAR(255),
    date_modified VARCHAR(255),
    event_status VARCHAR(255),
    start_date VARCHAR(255),
    end_date VARCHAR(255),
    previous_start_date VARCHAR(255),
    door_time VARCHAR(255),
    location_id BIGINT REFERENCES locations(id),
    event_attendance_mode VARCHAR(255),
    is_accessible_for_free BOOLEAN,
    promo_image VARCHAR(255),
    event_type VARCHAR(255),
    headliner_in_support BOOLEAN,
    custom_title VARCHAR(255),
    subtitle VARCHAR(255),
    time_record_was_entered TIMESTAMP,
    UNIQUE(identifier)
);

-- Table for Location
CREATE TABLE locations (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    identifier VARCHAR(255),
    url VARCHAR(255),
    image VARCHAR(255),
    date_published VARCHAR(255),
    date_modified VARCHAR(255),
    maximum_attendee_capacity INT,
    is_permanently_closed BOOLEAN,
    num_upcoming_events INT,
    UNIQUE(identifier)
);

-- Table for Offer
CREATE TABLE offers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    identifier VARCHAR(255),
    url VARCHAR(255),
    image VARCHAR(255),
    date_published VARCHAR(255),
    date_modified VARCHAR(255),
    category VARCHAR(255),
    price_specification_id BIGINT REFERENCES price_specifications(id),
    seller_id BIGINT REFERENCES sellers(id),
    valid_from VARCHAR(255),
    UNIQUE(identifier)
);

-- Table for UrlType
CREATE TABLE url_types (
    id SERIAL PRIMARY KEY,
    url_type VARCHAR(255),
    identifier VARCHAR(255),
    url VARCHAR(255),
    UNIQUE(identifier)
);

-- Table for Address
CREATE TABLE addresses (
    id SERIAL PRIMARY KEY,
    address_type VARCHAR(255),
    street_address VARCHAR(255),
    address_locality VARCHAR(255),
    postal_code VARCHAR(255),
    address_region_id BIGINT REFERENCES address_regions(id),
    address_country_id BIGINT REFERENCES address_countries(id),
    street_address2 VARCHAR(255),
    timezone VARCHAR(255),
    jam_base_metro_id INT,
    jam_base_city_id INT
);

-- Table for ExternalIdentifier
CREATE TABLE external_identifiers (
    id SERIAL PRIMARY KEY,
    source VARCHAR(255),
    identifier VARCHAR(255)
);

-- Table for GeoCoordinates
CREATE TABLE geo_coordinates (
    id SERIAL PRIMARY KEY,
    geo_coordinates_type VARCHAR(255),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION
);

-- Table for Performer
CREATE TABLE performers (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    identifier VARCHAR(255),
    url VARCHAR(255),
    image VARCHAR(255),
    date_published VARCHAR(255),
    date_modified VARCHAR(255),
    performer_type VARCHAR(255),
    founding_location_id BIGINT REFERENCES places(id),
    founding_date VARCHAR(255),
    band_or_musician VARCHAR(255),
    num_upcoming_events INT,
    performance_date VARCHAR(255),
    performance_rank INT,
    is_headliner BOOLEAN,
    date_is_confirmed BOOLEAN
);

-- Table for PriceSpecification
CREATE TABLE price_specifications (
    id SERIAL PRIMARY KEY,
    price_type VARCHAR(255),
    min_price DOUBLE PRECISION,
    max_price DOUBLE PRECISION,
    price DOUBLE PRECISION,
    price_currency VARCHAR(255)
);

-- Table for Seller
CREATE TABLE sellers (
    id SERIAL PRIMARY KEY,
    seller_type VARCHAR(255),
    identifier VARCHAR(255),
    disambiguating_description VARCHAR(255),
    name VARCHAR(255),
    url VARCHAR(255),
    image VARCHAR(255),
    date_published VARCHAR(255),
    date_modified VARCHAR(255)
);

-- Table for Place
CREATE TABLE places (
    id SERIAL PRIMARY KEY,
    place_type VARCHAR(255),
    name VARCHAR(255)
);

-- Table for AddressRegion
CREATE TABLE address_regions (
    id SERIAL PRIMARY KEY,
    address_region_type VARCHAR(255),
    identifier VARCHAR(255),
    name VARCHAR(255),
    alternate_name VARCHAR(255)
);

-- Table for AddressCountry
CREATE TABLE address_countries (
    id SERIAL PRIMARY KEY,
    address_country_type VARCHAR(255),
    identifier VARCHAR(255),
    name VARCHAR(255),
    alternate_name VARCHAR(255)
);

-- Table for MusicEvent-Location Many-to-Many Relationship
CREATE TABLE music_event_location (
    music_event_id BIGINT REFERENCES music_events(id),
    location_id BIGINT REFERENCES locations(id),
    PRIMARY KEY (music_event_id, location_id)
);

-- Table for MusicEvent-Offer Many-to-Many Relationship
CREATE TABLE music_event_offer (
    music_event_id BIGINT REFERENCES music_events(id),
    offer_id BIGINT REFERENCES offers(id),
    PRIMARY KEY (music_event_id, offer_id)
);

-- Table for MusicEvent-Performer Many-to-Many Relationship
CREATE TABLE music_event_performer (
    music_event_id BIGINT REFERENCES music_events(id),
    performer_id BIGINT REFERENCES performers(id),
    PRIMARY KEY (music_event_id, performer_id)
);

-- Table for Performer-Member Many-to-Many Relationship
CREATE TABLE performer_member (
    performer_id BIGINT REFERENCES performers(id),
    member_id BIGINT REFERENCES performers(id),
    PRIMARY KEY (performer_id, member_id)
);

-- Table for Performer-MemberOf Many-to-Many Relationship
CREATE TABLE performer_member_of (
    performer_id BIGINT REFERENCES performers(id),
    member_of_id BIGINT REFERENCES performers(id),
    PRIMARY KEY (performer_id, member_of_id)
);

-- Table for MusicEvent-ExternalIdentifier Many-to-Many Relationship
CREATE TABLE music_event_external_identifier (
    music_event_id BIGINT REFERENCES music_events(id),
    external_identifier_id BIGINT REFERENCES external_identifiers(id),
    PRIMARY KEY (music_event_id, external_identifier_id)
);
