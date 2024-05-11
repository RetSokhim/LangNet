CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS user_tb (
    user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(100) NOT NULL ,
    email VARCHAR(100) UNIQUE NOT NULL ,
    password VARCHAR(150) NOT NULL ,
    firstname VARCHAR(50) NOT NULL ,
    lastname VARCHAR(50) NOT NULL ,
    birth_date TIMESTAMP NOT NULL ,
    image VARCHAR(255) DEFAULT null,
    gender VARCHAR(7) NOT NULL ,
    facebook VARCHAR(30) DEFAULT NULL,
    phone_number VARCHAR(20) DEFAULT NULL,
    telegram VARCHAR(30) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS otps_tb(
    otps_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    otps_code INTEGER,
    verify BOOLEAN DEFAULT false,
    active BOOLEAN DEFAULT true,
    issued_at TIMESTAMP DEFAULT now(),
    expiration_date TIMESTAMP,
    user_id UUID REFERENCES user_tb(user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

SELECT * FROM otps_tb WHERE user_id = '90e99201-d267-427a-9764-cd6565c663e6' ORDER BY issued_at LIMIT 1