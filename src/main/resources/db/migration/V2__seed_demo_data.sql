INSERT INTO app_user (id, username, display_name)
VALUES (0, 'demo', 'Demo User');

INSERT INTO deck (id, app_user_id, name, description)
VALUES (0, 0, 'Základné slovíčka SK‑CZ', 'Demo balíček 10 slovíčok');

INSERT INTO flashcard (deck_id, front_text, back_text) VALUES
 (0,'kniha','knížka'),
 (0,'dom','dům'),
 (0,'dievča','dívka'),
 (0,'chlapec','chlapec'),
 (0,'auto','auto'),
 (0,'mesto','město'),
 (0,'zlý','zlý'),
 (0,'dobrý','dobrý'),
 (0,'jablko','jablko'),
 (0,'voda','voda');
