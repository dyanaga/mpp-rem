create table agent_listing (
    listing_id varchar(255) not null,
    user_id    varchar(255) not null,
    primary key (listing_id, user_id)
);
create table app_user (
    user_id         varchar(255) not null,
    bio             varchar(500),
    birthday        timestamp,
    email           varchar(100) not null,
    gender          varchar(100),
    location        varchar(100) not null,
    name            varchar(100) not null,
    page_preference integer DEFAULT 10,
    phone_number    varchar(20),
    type            varchar(50)  not null,
    url             varchar(100),
    primary key (user_id)
);
create table listing (
    listing_id      varchar(255) not null,
    address         varchar(200),
    creator         varchar(255) not null,
    description     varchar(500),
    name            varchar(100),
    neighbourhood   varchar(40),
    rooms           int4,
    size            int4,
    suggested_price int4,
    primary key (listing_id)
);
create table offer (
    offer_id   varchar(255) not null,
    comment    varchar(500),
    listing_id varchar(255) not null,
    price      int4,
    timestamp  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id    varchar(255) not null,
    primary key (offer_id)
);
create table review (
    review_id varchar(255) not null,
    creator   varchar(255) not null,
    review    varchar(500),
    stars     int4,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id   varchar(255) not null,
    primary key (review_id)
);
create table user_login (
    user_login_id varchar(255) not null,
    is_active     BOOLEAN   DEFAULT TRUE,
    password      varchar(100),
    timestamp     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    username      varchar(50)  not null,
    primary key (user_login_id)
);
