-- the first script for migration
CREATE TABLE Profile (
  id       SERIAL PRIMARY KEY NOT NULL,
  username VARCHAR(256),
  password VARCHAR(256)
);

CREATE TABLE GameState (
  id       SERIAL PRIMARY KEY NOT NULL,
  username VARCHAR(256),
  guesses  VARCHAR(26),
  word     VARCHAR(256)
);