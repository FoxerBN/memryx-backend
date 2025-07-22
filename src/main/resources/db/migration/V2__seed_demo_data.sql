INSERT INTO app_user (id, username, display_name)
VALUES (1, 'demo', 'Demo User');

INSERT INTO deck (id, app_user_id, name, description)
VALUES (1, 1, 'Základné slovíčka SK‑CZ', 'Demo balíček 10 slovíčok');

INSERT INTO flashcard (deck_id, front_text, back_text) VALUES
 (1,'kniha','knížka'),
 (1,'dom','dům'),
 (1,'dievča','dívka'),
 (1,'chlapec','chlapec'),
 (1,'auto','auto'),
 (1,'mesto','město'),
 (1,'zlý','zlý'),
 (1,'dobrý','dobrý'),
 (1,'jablko','jablko'),
 (1,'voda','voda');
