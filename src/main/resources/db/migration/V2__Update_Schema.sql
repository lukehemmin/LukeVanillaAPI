-- 이 마이그레이션은 이전 버전의 엔티티와 데이터베이스 스키마 간의 불일치를 해결합니다.

-- player_auth 테이블에 is_first 열 추가(필요한 경우)
-- ALTER TABLE player_auth ADD COLUMN IF NOT EXISTS is_first BOOLEAN NOT NULL DEFAULT TRUE;
