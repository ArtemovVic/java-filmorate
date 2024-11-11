CREATE TABLE IF NOT EXISTS genres (
    genre_id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS rating_mpa (
    rating_id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating_name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    film_id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    duration INT NOT NULL,
    release_date DATE NOT NULL,
    rating_id INT REFERENCES rating_mpa(rating_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
    user_id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL UNIQUE,
    user_name VARCHAR(255),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id INT NOT NULL REFERENCES films (film_id) ON DELETE CASCADE ON UPDATE CASCADE,
    genre_id INT NOT NULL REFERENCES genres (genre_id) ON DELETE CASCADE ON UPDATE CASCADE

);

CREATE TABLE IF NOT EXISTS likes (
    film_id INT NOT NULL REFERENCES films (film_id) ON DELETE CASCADE ON UPDATE CASCADE,
    user_id INT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE ON UPDATE CASCADE

);

CREATE TABLE IF NOT EXISTS friends (
    user_id INT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    friend_id INT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    status BOOLEAN NOT NULL

);