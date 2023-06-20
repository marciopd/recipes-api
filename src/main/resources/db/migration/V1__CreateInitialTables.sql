CREATE TABLE "user"
(
    id       int          NOT NULL AUTO_INCREMENT,
    username varchar(20)  NOT NULL,
    password varchar(100) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (username)
);

CREATE TABLE user_role
(
    id        int         NOT NULL AUTO_INCREMENT,
    user_id   int         NOT NULL,
    role_name varchar(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (user_id, role_name),
    FOREIGN KEY (user_id) REFERENCES "user" (id)
);

CREATE TABLE recipe
(
    id                int          NOT NULL AUTO_INCREMENT,
    number_servings   int          NOT NULL,
    title             varchar(50)  NOT NULL,
    short_description varchar(300) NOT NULL,
    instructions      TEXT         NOT NULL,
    creation_time     TIMESTAMP    NOT NULL,
    user_id           int          NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (title),
    FOREIGN KEY (user_id) REFERENCES "user" (id)
);

CREATE TABLE tag
(
    id   int         NOT NULL AUTO_INCREMENT,
    name varchar(10) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (name)
);

CREATE TABLE recipe_tag
(
    id        int NOT NULL AUTO_INCREMENT,
    tag_id    int NOT NULL,
    recipe_id int NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (recipe_id, tag_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe (id),
    FOREIGN KEY (tag_id) REFERENCES tag (id)
);

CREATE TABLE recipe_ingredient
(
    id        int         NOT NULL AUTO_INCREMENT,
    recipe_id int         NOT NULL,
    text      varchar(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (recipe_id, text),
    FOREIGN KEY (recipe_id) REFERENCES recipe (id)
);


insert into tag (name) values ('vegetarian');
insert into tag (name) values ('vegan');
insert into tag (name) values ('spicy');