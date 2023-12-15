DROP DATABASE music_events_near_me;
CREATE DATABASE music_events_near_me;

USE music_events_near_me;

-- Table: music_events
CREATE TABLE
    music_events (
        id BIGINT NOT NULL AUTO_INCREMENT,
        date_modified VARCHAR(255),
        date_published VARCHAR(255),
        door_time VARCHAR(255),
        end_date VARCHAR(255),
        event_attendance_mode VARCHAR(255),
        event_status VARCHAR(255),
        event_type VARCHAR(255),
        headliner_in_support BOOLEAN,
        identifier VARCHAR(255),
        image VARCHAR(255),
        is_accessible_for_free BOOLEAN,
        name VARCHAR(255),
        previous_start_date VARCHAR(255),
        promo_image VARCHAR(255),
        start_date VARCHAR(255),
        subtitle VARCHAR(255),
        url VARCHAR(255),
        time_record_was_entered VARCHAR(255),
        PRIMARY KEY (id)
    );

-- Table: addresses
CREATE TABLE
    addresses (
        id BIGINT NOT NULL AUTO_INCREMENT,
        address_country_id BIGINT,
        address_region_id BIGINT,
        address_locality VARCHAR(255),
        address_type VARCHAR(255),
        jam_base_city_id INTEGER,
        jam_base_metro_id INTEGER,
        postal_code VARCHAR(255),
        street_address VARCHAR(255),
        street_address2 VARCHAR(255),
        timezone VARCHAR(255),
        PRIMARY KEY (id)
    );

-- Table: address_countries
CREATE TABLE
    address_countries (
        id BIGINT NOT NULL AUTO_INCREMENT,
        address_country_type VARCHAR(255),
        alternate_name VARCHAR(255),
        identifier VARCHAR(255),
        name VARCHAR(255),
        PRIMARY KEY (id)
    );

-- Table: address_regions
CREATE TABLE
    address_regions (
        id BIGINT NOT NULL AUTO_INCREMENT,
        address_region_type VARCHAR(255),
        alternate_name VARCHAR(255),
        identifier VARCHAR(255),
        name VARCHAR(255),
        PRIMARY KEY (id)
    );

-- Table: external_identifiers
CREATE TABLE
    external_identifiers (
        id BIGINT NOT NULL AUTO_INCREMENT,
        identifier VARCHAR(255),
        source VARCHAR(255),
        PRIMARY KEY (id)
    );

-- Table: geo_coordinates
CREATE TABLE
    geo_coordinates (
        id BIGINT NOT NULL AUTO_INCREMENT,
        geo_coordinates_type VARCHAR(255),
        latitude DOUBLE PRECISION,
        longitude DOUBLE PRECISION,
        PRIMARY KEY (id)
    );

-- Table: locations
CREATE TABLE
    locations (
        id BIGINT NOT NULL AUTO_INCREMENT,
        date_modified VARCHAR(255),
        date_published VARCHAR(255),
        identifier VARCHAR(255),
        image VARCHAR(255),
        is_permanently_closed BOOLEAN,
        jam_base_city_id INTEGER,
        jam_base_metro_id INTEGER,
        maximum_attendee_capacity INTEGER,
        name VARCHAR(255),
        num_upcoming_events INTEGER,
        url VARCHAR(255),
        PRIMARY KEY (id)
    );

-- Table: offers
CREATE TABLE
    offers (
        id BIGINT NOT NULL AUTO_INCREMENT,
        category VARCHAR(255),
        date_modified VARCHAR(255),
        date_published VARCHAR(255),
        identifier VARCHAR(255),
        image VARCHAR(255),
        name VARCHAR(255),
        url VARCHAR(255),
        valid_from VARCHAR(255),
        PRIMARY KEY (id)
    );

-- Table: performers
CREATE TABLE
    performers (
        id BIGINT NOT NULL AUTO_INCREMENT,
        band_or_musician VARCHAR(255),
        date_is_confirmed BOOLEAN,
        date_modified VARCHAR(255),
        date_published VARCHAR(255),
        founding_date VARCHAR(255),
        identifier VARCHAR(255),
        image VARCHAR(255),
        is_headliner BOOLEAN,
        jam_base_city_id INTEGER,
        jam_base_metro_id INTEGER,
        name VARCHAR(255),
        num_upcoming_events INTEGER,
        performance_date VARCHAR(255),
        performance_rank INTEGER,
        performer_type VARCHAR(255),
        url VARCHAR(255),
        PRIMARY KEY (id)
    );

-- Table: places
CREATE TABLE
    places (
        id BIGINT NOT NULL AUTO_INCREMENT,
        name VARCHAR(255),
        place_type VARCHAR(255),
        PRIMARY KEY (id)
    );

-- Table: price_specifications
CREATE TABLE
    price_specifications (
        id BIGINT NOT NULL AUTO_INCREMENT,
        max_price DOUBLE PRECISION,
        min_price DOUBLE PRECISION,
        price DOUBLE PRECISION,
        price_currency VARCHAR(255),
        price_type VARCHAR(255),
        PRIMARY KEY (id)
    );

-- Table: sellers
CREATE TABLE
    sellers (
        id BIGINT NOT NULL AUTO_INCREMENT,
        date_modified VARCHAR(255),
        date_published VARCHAR(255),
        disambiguating_description VARCHAR(255),
        identifier VARCHAR(255),
        image VARCHAR(255),
        name VARCHAR(255),
        seller_type VARCHAR(255),
        url VARCHAR(255),
        PRIMARY KEY (id)
    );

-- Table: url_types
CREATE TABLE
    url_types (
        id BIGINT NOT NULL AUTO_INCREMENT,
        identifier VARCHAR(255),
        type VARCHAR(255),
        url VARCHAR(255),
        PRIMARY KEY (id)
    );

-- Foreign key: FKnc7fqw6ukn54y63tn72jg6i65
ALTER TABLE addresses ADD CONSTRAINT FKnc7fqw6ukn54y63tn72jg6i65 FOREIGN KEY (address_country_id) REFERENCES address_countries (id);

-- Foreign key: FKq82me5ftvj4p98wh1etfv3cv
ALTER TABLE addresses ADD CONSTRAINT FKq82me5ftvj4p98wh1etfv3cv FOREIGN KEY (address_region_id) REFERENCES address_regions (id);

-- Foreign key: FKho5qr9gb8msoi0xkvwb4f90cm
ALTER TABLE locations ADD CONSTRAINT FKho5qr9gb8msoi0xkvwb4f90cm FOREIGN KEY (address_id) REFERENCES addresses (id);

-- Foreign key: FKgdnvfy1cs09y2rv4l8vj7nbiq
ALTER TABLE locations ADD CONSTRAINT FKgdnvfy1cs09y2rv4l8vj7nbiq FOREIGN KEY (geo_id) REFERENCES geo_coordinates (id);

-- Foreign key: FKcq55s69a7e8nd9gmf4fexr7n
ALTER TABLE offers ADD CONSTRAINT FKcq55s69a7e8nd9gmf4fexr7n FOREIGN KEY (price_specification_id) REFERENCES price_specifications (id);

-- Foreign key: FK7l1jcny3c3w6snu5wyc3wh3f
ALTER TABLE offers ADD CONSTRAINT FK7l1jcny3c3w6snu5wyc3wh3f FOREIGN KEY (seller_id) REFERENCES sellers (id);

-- Foreign key: FKud2wtbe49u9y3rbcfc9kn7s9
ALTER TABLE performers ADD CONSTRAINT FKud2wtbe49u9y3rbcfc9kn7s9 FOREIGN KEY (founding_location_id) REFERENCES places (id);

-- Foreign key: FKwsgvbhfnw2o4tmr2brqjnvpm
ALTER TABLE performers ADD CONSTRAINT FKwsgvbhfnw2o4tmr2brqjnvpm FOREIGN KEY (jam_base_city_id) REFERENCES addresses (jam_base_city_id);

-- Foreign key: FKauktls85ylb7v6mj5sr9apcjd
ALTER TABLE performers ADD CONSTRAINT FKauktls85ylb7v6mj5sr9apcjd FOREIGN KEY (jam_base_metro_id) REFERENCES addresses (jam_base_metro_id);

-- Foreign key: FK6rjujwvl7h0n0i0v7i3ydrfb
ALTER TABLE performers ADD CONSTRAINT FK6rjujwvl7h0n0i0v7i3ydrfb FOREIGN KEY (place_id) REFERENCES places (id);

-- Foreign key: FKbej7m96d4jir61vqsv14uey5u
ALTER TABLE performers ADD CONSTRAINT FKbej7m96d4jir61vqsv14uey5u FOREIGN KEY (performer_id) REFERENCES performers (id);

-- Foreign key: FK5lqti2v0vkgjqjlfvdbn9oet
ALTER TABLE places ADD CONSTRAINT FK5lqti2v0vkgjqjlfvdbn9oet FOREIGN KEY (address_id) REFERENCES addresses (id);

-- Foreign key: FK53lbc7gr1h9rmk3a5r7vxx3ea
ALTER TABLE places ADD CONSTRAINT FK53lbc7gr1h9rmk3a5r7vxx3ea FOREIGN KEY (geo_id) REFERENCES geo_coordinates (id);

-- Foreign key: FK2e0aobd0l8i11kumv3tw0sgfp
ALTER TABLE external_identifiers ADD CONSTRAINT FK2e0aobd0l8i11kumv3tw0sgfp FOREIGN KEY (performer_id) REFERENCES performers (id);

-- Foreign key: FK9lxmq9t19a8omcbgabm9rju69
ALTER TABLE locations ADD CONSTRAINT FK9lxmq9t19a8omcbgabm9rju69 FOREIGN KEY (external_identifier_id) REFERENCES external_identifiers (id);