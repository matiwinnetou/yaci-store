drop table if exists assets;
create table assets
(
    id bigint not null auto_increment
       primary key,
    slot                  bigint,
    tx_hash               varchar(255) not null,
    policy                varchar(255),
    asset_name            varchar(255),
    unit                  varchar(255),
    quantity              bigint,
    mint_type             varchar(4),
    create_datetime       timestamp,
    update_datetime       timestamp
);

CREATE INDEX idx_assets_tx_hash
    ON assets(tx_hash);

CREATE INDEX idx_assets_policy
    ON assets(policy);

CREATE INDEX idx_assets_policy_assetname
    ON assets(policy, asset_name);
