create table product
(
    id          int primary key not null generated by default as identity,
    name        varchar         not null,
    description varchar default '',
    cost        float           not null,
    status      varchar         not null,
    seller_id   int             not null references users (id),
    buyer_id    int references users (id)
)