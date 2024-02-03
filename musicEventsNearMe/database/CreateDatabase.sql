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
    datePublished VARCHAR(255),
    dateModified VARCHAR(255),
    performerType VARCHAR(255),
    foundingLocationId BIGINT,
    foundingDate VARCHAR(255),
    genre JSON,
    bandOrMusician VARCHAR(255),
    numUpcomingEvents INT,
    performanceDate VARCHAR(255),
    performanceRank INT,
    isHeadliner BOOLEAN,
    dateIsConfirmed BOOLEAN,
    timeRecordWasEntered DATETIME
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
    datePublished VARCHAR(255),
    dateModified VARCHAR(255),
    eventStatus VARCHAR(255),
    startDate DATETIME,
    endDate VARCHAR(255),
    previousStartDate VARCHAR(255),
    doorTime VARCHAR(255),
    locationId BIGINT,
    eventAttendanceMode VARCHAR(255),
    isAccessibleForFree BOOLEAN,
    promoImage VARCHAR(255),
    eventType VARCHAR(255),
    streamIds JSON,
    headlinerInSupport BOOLEAN,
    customTitle VARCHAR(255),
    subtitle VARCHAR(255),
    timeRecordWasEntered DATETIME
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
    priceCurrency VARCHAR(255)
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
    datePublished VARCHAR(255),
    dateModified VARCHAR(255),
    maximumAttendeeCapacity INT,
    address_id BIGINT, -- Use address_id instead of addressId
    isPermanentlyClosed BOOLEAN,
    numUpcomingEvents INT,
    timeRecordWasEntered DATETIME
);

-- Table: addresses
CREATE TABLE IF NOT EXISTS addresses (
    address_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    addressType VARCHAR(255),
    streetAddress VARCHAR(255),
    addressLocality VARCHAR(255),
    postalCode VARCHAR(255),
    address_region_id BIGINT, -- Use address_region_id instead of addressRegionId
    address_country_id BIGINT, -- Use address_country_id instead of addressCountryId
    streetAddress2 VARCHAR(255),
    timezone VARCHAR(255),
    jamBaseMetroId INT,
    jamBaseCityId INT
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


