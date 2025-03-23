CREATE TABLE IF NOT EXISTS player_balance (
    uuid VARCHAR(36) PRIMARY KEY,
    balance DECIMAL(20, 2) NOT NULL DEFAULT 0.0
);

CREATE TABLE IF NOT EXISTS player_auth (
    auth_code VARCHAR(6) PRIMARY KEY,
    uuid VARCHAR(36) NOT NULL,
    is_auth BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS player_data (
    uuid VARCHAR(36) PRIMARY KEY,
    nickname VARCHAR(255) NOT NULL,
    discord_id VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS player_nametag (
    uuid VARCHAR(36) PRIMARY KEY,
    tag VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS support_chat_link (
    support_id VARCHAR(36) PRIMARY KEY,
    uuid VARCHAR(36) NOT NULL,
    message_link TEXT NOT NULL,
    case_close BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 추가
CREATE INDEX IF NOT EXISTS idx_player_data_discord_id ON player_data(discord_id);
CREATE INDEX IF NOT EXISTS idx_player_auth_uuid ON player_auth(uuid);
CREATE INDEX IF NOT EXISTS idx_support_chat_link_uuid ON support_chat_link(uuid);
