-- Create database
DROP DATABASE IF EXISTS music_events_near_me;
CREATE DATABASE music_events_near_me;
USE music_events_near_me;

-- Table: performers
CREATE TABLE IF NOT EXISTS performers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    identifier VARCHAR(255),
    url VARCHAR(255),
    image VARCHAR(255),
    date_published VARCHAR(255),
    date_modified VARCHAR(255),
    performer_type VARCHAR(255),
    founding_location_id BIGINT,
    founding_date VARCHAR(255),
    genre JSON,
    band_or_musician VARCHAR(255),
    num_upcoming_events INT,
    performance_date VARCHAR(255),
    performance_rank INT,
    is_headliner BOOLEAN,
    date_is_confirmed BOOLEAN,
    time_record_was_entered DATETIME
);

-- Table: performances
CREATE TABLE IF NOT EXISTS performances (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    performer_id BIGINT,
    event_id BIGINT,
    FOREIGN KEY (performer_id) REFERENCES performers(id),
    FOREIGN KEY (event_id) REFERENCES music_events(id)
);

-- Table: music_events
CREATE TABLE IF NOT EXISTS music_events (
    music_event_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    identifier VARCHAR(255),
    url VARCHAR(255),
    image VARCHAR(255),
    date_published VARCHAR(255),
    date_modified VARCHAR(255),
    event_status VARCHAR(255),
    start_date DATETIME,
    end_date VARCHAR(255),
    previous_start_date VARCHAR(255),
    door_time VARCHAR(255),
    location_id BIGINT,
    event_attendance_mode VARCHAR(255),
    is_accessible_for_free BOOLEAN,
    promo_image VARCHAR(255),
    event_type VARCHAR(255),
    stream_ids JSON,
    headliner_in_support BOOLEAN,
    custom_title VARCHAR(255),
    subtitle VARCHAR(255),
    time_record_was_entered DATETIME
);

-- Table: offers
CREATE TABLE IF NOT EXISTS offers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    identifier VARCHAR(255),
    url VARCHAR(255),
    date_published VARCHAR(255),
    date_modified VARCHAR(255),
    category VARCHAR(255),
    price_specification_id BIGINT,
    seller_id BIGINT,
    valid_from VARCHAR(255),
    FOREIGN KEY (price_specification_id) REFERENCES price_specifications(id),
    FOREIGN KEY (seller_id) REFERENCES sellers(id)
);

-- Table: price_specifications
CREATE TABLE IF NOT EXISTS price_specifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    price DOUBLE,
    price_currency VARCHAR(255)
);

-- Table: sellers
CREATE TABLE IF NOT EXISTS sellers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    identifier VARCHAR(255),
    name VARCHAR(255)
);

-- Table: locations
CREATE TABLE IF NOT EXISTS locations (
    location_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    identifier VARCHAR(255),
    url VARCHAR(255),
    image VARCHAR(255),
    date_published VARCHAR(255),
    date_modified VARCHAR(255),
    maximum_attendee_capacity INT,
    address_id BIGINT, -- Use address_id instead of addressId
    is_permanently_closed BOOLEAN,
    num_upcoming_events INT,
    time_record_was_entered DATETIME
);

-- Table: addresses
CREATE TABLE IF NOT EXISTS addresses (
    address_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    address_type VARCHAR(255),
    street_address VARCHAR(255),
    address_locality VARCHAR(255),
    postal_code VARCHAR(255),
    address_region_id BIGINT,
    address_country_id BIGINT,
    street_address2 VARCHAR(255),
    timezone VARCHAR(255),
    jam_base_metro_id INT,
    jam_base_city_id INT
);

-- Table: geo_coordinates
CREATE TABLE IF NOT EXISTS geo_coordinates (
    geo_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    latitude DOUBLE,
    longitude DOUBLE,
    location_id BIGINT -- Use location_id instead of locationId
);

-- Table: address_countries
CREATE TABLE IF NOT EXISTS address_countries (
    address_country_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    identifier VARCHAR(255),
    name VARCHAR(255)
);

-- Table: address_regions
CREATE TABLE IF NOT EXISTS address_regions (
    address_region_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    identifier VARCHAR(255),
    name VARCHAR(255)
);


