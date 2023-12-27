-- Create database
DROP DATABASE music_events_near_me;
CREATE DATABASE IF NOT EXISTS music_events_near_me;
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
    eventsId JSON,
    bandOrMusician VARCHAR(255),
    numUpcomingEvents INT,
    performanceDate VARCHAR(255),
    performanceRank INT,
    isHeadliner BOOLEAN,
    dateIsConfirmed BOOLEAN,
    timeRecordWasEntered DATETIME
);

-- Table: music_events
CREATE TABLE IF NOT EXISTS music_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    identifier VARCHAR(255),
    url VARCHAR(255),
    image VARCHAR(255),
    datePublished VARCHAR(255),
    dateModified VARCHAR(255),
    eventStatus VARCHAR(255),
    startDate VARCHAR(255),
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
    performers_id VARBINARY(1028), -- Add this line to include the new column
    timeRecordWasEntered DATETIME
);

-- Table: offers
CREATE TABLE IF NOT EXISTS offers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    identifier VARCHAR(255),
    url VARCHAR(255),
    image VARCHAR(255),
    datePublished VARCHAR(255),
    dateModified VARCHAR(255),
    category VARCHAR(255),
    priceSpecificationId BIGINT,
    sellerId BIGINT,
    validFrom VARCHAR(255)
);

-- Table: url_types
CREATE TABLE IF NOT EXISTS url_types (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    urlType VARCHAR(255),
    identifier VARCHAR(255),
    url VARCHAR(255)
);

-- Table: external_identifiers
CREATE TABLE IF NOT EXISTS external_identifiers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    source VARCHAR(255),
    identifier VARCHAR(255)
);

-- Table: price_specifications
CREATE TABLE IF NOT EXISTS price_specifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    priceType VARCHAR(255),
    minPrice DOUBLE,
    maxPrice DOUBLE,
    price DOUBLE,
    priceCurrency VARCHAR(255)
);

-- Table: sellers
CREATE TABLE IF NOT EXISTS sellers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sellerType VARCHAR(255),
    identifier VARCHAR(255),
    disambiguatingDescription VARCHAR(255),
    name VARCHAR(255),
    url VARCHAR(255),
    image VARCHAR(255),
    datePublished VARCHAR(255),
    dateModified VARCHAR(255)
);

-- Table: locations
CREATE TABLE IF NOT EXISTS locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    identifier VARCHAR(255),
    url VARCHAR(255),
    image VARCHAR(255),
    datePublished VARCHAR(255),
    dateModified VARCHAR(255),
    maximumAttendeeCapacity INT,
    addressId BIGINT,
    geoId BIGINT,
    eventsId JSON,
    isPermanentlyClosed BOOLEAN,
    numUpcomingEvents INT,
    externalIdentifiersId JSON,
    timeRecordWasEntered DATETIME
);

-- Table: addresses
CREATE TABLE IF NOT EXISTS addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    addressType VARCHAR(255),
    streetAddress VARCHAR(255),
    addressLocality VARCHAR(255),
    postalCode VARCHAR(255),
    addressRegionId BIGINT,
    addressCountryId BIGINT,
    streetAddress2 VARCHAR(255),
    timezone VARCHAR(255),
    jamBaseMetroId INT,
    jamBaseCityId INT
);

-- Table: geo_coordinates
CREATE TABLE IF NOT EXISTS geo_coordinates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    geoCoordinatesType VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE
);

-- Table: address_countries
CREATE TABLE IF NOT EXISTS address_countries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    addressCountryType VARCHAR(255),
    identifier VARCHAR(255),
    name VARCHAR(255),
    alternateName VARCHAR(255)
);

-- Table: address_regions
CREATE TABLE IF NOT EXISTS address_regions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    addressRegionType VARCHAR(255),
    identifier VARCHAR(255),
    name VARCHAR(255),
    alternateName VARCHAR(255)
);

-- Table: places
CREATE TABLE IF NOT EXISTS places (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    placeType VARCHAR(255),
    name VARCHAR(255)
);

-- Foreign Key Constraints
ALTER TABLE performers ADD CONSTRAINT fk_performers_foundingLocation FOREIGN KEY (foundingLocationId) REFERENCES places(id);
ALTER TABLE music_events ADD CONSTRAINT fk_music_events_location FOREIGN KEY (locationId) REFERENCES locations(id);
ALTER TABLE offers ADD CONSTRAINT fk_offers_priceSpecification FOREIGN KEY (priceSpecificationId) REFERENCES price_specifications(id);
ALTER TABLE offers ADD CONSTRAINT fk_offers_seller FOREIGN KEY (sellerId) REFERENCES sellers(id);
ALTER TABLE locations ADD CONSTRAINT fk_locations_address FOREIGN KEY (addressId) REFERENCES addresses(id);
ALTER TABLE locations ADD CONSTRAINT fk_locations_geo FOREIGN KEY (geoId) REFERENCES geo_coordinates(id);
ALTER TABLE locations ADD CONSTRAINT fk_locations_externalIdentifiers FOREIGN KEY (externalIdentifiersId) REFERENCES external_identifiers(id);
ALTER TABLE addresses ADD CONSTRAINT fk_addresses_addressRegion FOREIGN KEY (addressRegionId) REFERENCES address_regions(id);
ALTER TABLE addresses ADD CONSTRAINT fk_addresses_addressCountry FOREIGN KEY (addressCountryId) REFERENCES address_countries(id);

-- End of Script
