CREATE TABLE app_user (
  id          BIGSERIAL PRIMARY KEY,
  username    VARCHAR(50) UNIQUE NOT NULL,
  display_name VARCHAR(100)
);

CREATE TABLE deck (
  id          BIGSERIAL PRIMARY KEY,
  app_user_id BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  name        VARCHAR(100) NOT NULL,
  description TEXT
);

CREATE TABLE flashcard (
  id          BIGSERIAL PRIMARY KEY,
  deck_id     BIGINT NOT NULL REFERENCES deck(id) ON DELETE CASCADE,
  front_text  TEXT NOT NULL,
  back_text   TEXT NOT NULL
);
