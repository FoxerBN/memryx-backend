CREATE TABLE app_user (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  display_name VARCHAR(100) NOT NULL
);

CREATE TABLE folder (
  id BIGSERIAL PRIMARY KEY,
  app_user_id BIGINT NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  name VARCHAR(100) NOT NULL
);

CREATE TABLE deck (
  id BIGSERIAL PRIMARY KEY,
  folder_id BIGINT NOT NULL REFERENCES folder(id) ON DELETE CASCADE,
  name VARCHAR(100) NOT NULL,
  description TEXT
);

CREATE TABLE flashcard (
  id BIGSERIAL PRIMARY KEY,
  deck_id BIGINT NOT NULL REFERENCES deck(id) ON DELETE CASCADE,
  front_text TEXT NOT NULL,
  back_text TEXT NOT NULL
);

INSERT INTO app_user (username, display_name)
VALUES ('demo', 'Demo User');

INSERT INTO folder (app_user_id, name)
VALUES
  ((SELECT id FROM app_user WHERE username = 'demo'), 'Jazyky');

INSERT INTO deck (folder_id, name, description)
VALUES
  ((SELECT id FROM folder WHERE name = 'Jazyky' AND app_user_id = (SELECT id FROM app_user WHERE username = 'demo')),
   'Základné slovíčka SK‑CZ', 'Demo balíček 10 slovíčok'),
  ((SELECT id FROM folder WHERE name = 'Jazyky' AND app_user_id = (SELECT id FROM app_user WHERE username = 'demo')),
   'Základné slovíčka SK‑EN', 'Demo balíček 13 slovíčok');

INSERT INTO flashcard (deck_id, front_text, back_text) VALUES
  (1, 'kniha', 'knížka'),
  (1, 'dom', 'dům'),
  (1, 'dievča', 'dívka'),
  (1, 'chlapec', 'chlapec'),
  (1, 'auto', 'auto'),
  (1, 'mesto', 'město'),
  (1, 'zlý', 'zlý'),
  (1, 'dobrý', 'dobrý'),
  (1, 'jablko', 'jablko'),
  (1, 'voda', 'voda'),

  (2, 'kniha', 'book'),
  (2, 'dom', 'house'),
  (2, 'dievča', 'girl'),
  (2, 'chlapec', 'boy'),
  (2, 'auto', 'car'),
  (2, 'mesto', 'city'),
  (2, 'zlý', 'bad'),
  (2, 'dobrý', 'good'),
  (2, 'jablko', 'apple'),
  (2, 'voda', 'water'),
  (2, 'pes', 'dog'),
  (2, 'mačka', 'cat'),
  (2, 'strom', 'tree');
