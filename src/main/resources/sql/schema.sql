CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS user_tb
(
    user_id    UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username   VARCHAR(100) UNIQUE NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(150)        NOT NULL,
    firstname  VARCHAR(50)      DEFAULT NULL,
    lastname   VARCHAR(50)      DEFAULT NULL,
    birth_date TIMESTAMP        DEFAULT NULL,
    image      VARCHAR(255)     DEFAULT NULL,
    gender     VARCHAR(7)       DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS contact_tb
(
    contact_id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    facebook     VARCHAR(30)      DEFAULT NULL,
    phone_number VARCHAR(20)      DEFAULT NULL,
    telegram     VARCHAR(30)      DEFAULT NULL,
    user_id      UUID REFERENCES user_tb (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS otps_tb
(
    otps_id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    otps_code       INTEGER NOT NULL,
    verify          BOOLEAN          DEFAULT false,
    issued_at       TIMESTAMP        DEFAULT now(),
    expiration_date TIMESTAMP,
    user_id         UUID REFERENCES user_tb (user_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS project_tb
(
    project_id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    project_name VARCHAR(100) NOT NULL,
    status       VARCHAR(15)      DEFAULT 'PENDING',
    active       BOOLEAN          DEFAULT true,
    create_date  TIMESTAMP        DEFAULT now(),
    updated_date TIMESTAMP        DEFAULT NULL,
    removed_date TIMESTAMP        DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS role_tb
(
    role_id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    role_name VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS project_member_tb
(
    member_id  UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    project_id UUID REFERENCES project_tb (project_id) ON DELETE CASCADE ON UPDATE CASCADE,
    user_id    UUID REFERENCES user_tb (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    role_id    UUID REFERENCES role_tb (role_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS notification_tb
(
    notification_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    project_id      UUID REFERENCES project_tb (project_id) ON DELETE CASCADE ON UPDATE CASCADE,
    sender_id       UUID REFERENCES user_tb (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    receiver_id     UUID REFERENCES user_tb (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    type            VARCHAR(20) NOT NULL,
    tile            VARCHAR(30) NOT NULL,
    description     TEXT        NOT NULL,
    is_read         BOOLEAN          default false,
    create_date     TIMESTAMP        DEFAULT now()
);

CREATE TABLE IF NOT EXISTS language_tb
(
    language_id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    language      VARCHAR(30) NOT NULL,
    language_code VARCHAR(3)  NOT NULL
);

CREATE TABLE IF NOT EXISTS language_project_tb
(
    language_project_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    language_id         UUID REFERENCES language_tb (language_id) ON DELETE CASCADE ON UPDATE CASCADE,
    project_id          UUID REFERENCES project_tb (project_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS attachment_tb
(
    attachment_id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    project_id      UUID REFERENCES project_tb (project_id) ON DELETE CASCADE ON UPDATE CASCADE,
    language_id     UUID REFERENCES language_tb (language_id) ON DELETE CASCADE ON UPDATE CASCADE,
    posted_by       UUID REFERENCES user_tb (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    attachment_name VARCHAR(100) NOT NULL,
    data            JSONB        NOT NULL,
    status          VARCHAR(15)      DEFAULT 'PENDING',
    posted_date     TIMESTAMP        DEFAULT now(),
    expire_date     TIMESTAMP        DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS hint_tb
(
    hint_id       UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    attachment_id UUID REFERENCES attachment_tb (attachment_id) ON DELETE CASCADE ON UPDATE CASCADE,
    provide_by    UUID REFERENCES user_tb (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    hint          TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS feedback_tb
(
    feedback_id   UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    attachment_id UUID REFERENCES attachment_tb (attachment_id) ON DELETE CASCADE ON UPDATE CASCADE,
    comment_by    UUID REFERENCES user_tb (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    comment       TEXT NOT NULL,
    active        BOOLEAN          DEFAULT true,
    created_date  TIMESTAMP        DEFAULT now(),
    removed_date  TIMESTAMP        DEFAULT NULL,
    edit_date     TIMESTAMP        DEFAULT NULL
);
-- CREATE TABLE IF NOT EXISTS excel_file_tb
-- (
--     id    SERIAL PRIMARY KEY,
--     key   VARCHAR(100),
--     VALUE VARCHAR(200) DEFAULT Null
-- );
--
-- CREATE TABLE IF NOT EXISTS excel_file_json_tb
-- (
--     id         UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
--     data jsonb
-- );