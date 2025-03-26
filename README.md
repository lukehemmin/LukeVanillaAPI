# LukeVanillaAPI

![LukeVanilla 로고](https://cdn.discordapp.com/attachments/1266886234907611159/1308189328970743858/29b804970c9d735c.png?ex=6775ba41&is=677468c1&hm=29b0d34074ec1cacbd59ff78b83458d5620c11b9f63dcb5403ed0360002da674&)

LukeVanilla 마인크래프트 서버를 위한 REST API 서버입니다. 이 프로젝트는 기존 LukeVanilla 플러그인의 기능을 점진적으로 API 서비스로 전환하기 위해 개발되었습니다.

## 개요

LukeVanillaAPI는 Spring Boot 기반의 RESTful API 서버로, LukeVanilla 마인크래프트 서버의 다양한 기능을 외부 서비스로 분리하여 더 안정적이고 확장 가능한 아키텍처를 제공합니다.

## 현재 구현된 기능

- **인증 시스템**
  - 플레이어 인증 코드 생성/검증 (`/api/auth/code`, `/api/auth/validate`)
  - 디스코드 연동 인증 기능
  - JWT 기반 토큰 인증

- **경제 시스템**
  - 플레이어 잔액 조회 (`/api/economy/balance/{uuid}`)
  - 잔액 추가/차감 기능 (`/api/economy/balance/{uuid}/add`, `//api/economy/balance/{uuid}/remove`)

- **플레이어 관리**
  - 디스코드 ID로 플레이어 정보 조회 (`/api/auth/player/discord/{discordId}`)

## LukeVanilla에서 API로 전환 필요한 기능

1. **데이터베이스 직접 접근 코드**
   - `Database.kt`의 플레이어 데이터 관련 쿼리를 JPA Repository로 마이그레이션
   - 플레이어 인증, 데이터 저장, 설정 관리 등의 기능 전환

2. **디스코드 통합 기능**
   - `DiscordBot.kt`, `DiscordVoiceChannelListener.kt`의 기능을 API로 전환
   - 디스코드 채널과 게임 내 채팅 연동 기능 구현
   - 음성 채널 상태 모니터링 및 게임 내 표시 기능

3. **글로벌 채팅 시스템**
   - `GlobalChatManager.kt`의 채팅 기능을 API 기반으로 전환
   - 서버 간 채팅 메시지 전송 및 관리 기능 구현

4. **VPN 감지 시스템**
   - `AntiVPN.kt`의 VPN 감지 로직을 API로 분리
   - IP 기반 프록시/VPN 감지 및 차단 기능 구현

5. **경제 시스템 완전 전환**
   - `EconomyManager.kt`의 나머지 기능 API 전환 완료
   - 거래 이력 저장 및 조회 기능 추가

## 추가 개발 필요 기능

1. **지원/티켓 시스템 API**
   - 플레이어 지원 요청 생성/관리
   - 관리자 응답 및 티켓 상태 업데이트
   - 지원 이력 조회

2. **플레이어 네임태그 확장 기능**
   - 네임태그 커스터마이징 옵션 추가
   - 구매 및 적용 API 제공

3. **서버 로깅 및 모니터링 API**
   - 서버 이벤트 로깅 시스템
   - 성능 모니터링 엔드포인트
   - 관리자용 통계 대시보드 API

4. **채팅 필터링 및 관리 기능**
   - 비속어 필터링 룰 관리
   - 채팅 로그 저장 및 분석

5. **이벤트 관리 시스템**
   - 서버 이벤트 생성 및 관리
   - 이벤트 참가자 등록 및 보상 지급

## API 문서

Swagger UI를 통해 API 문서를 확인할 수 있습니다:
- API 문서: `/swagger-ui.html`
- API 명세(JSON): `/api-docs`

## 기술 스택

- **Backend**: Spring Boot, Kotlin
- **데이터베이스**: PostgreSQL
- **보안**: Spring Security, JWT
- **문서화**: Swagger/OpenAPI
- **마이그레이션**: Flyway

## 개발 환경 설정

1. 필수 요구사항:
   - JDK 17
   - PostgreSQL 데이터베이스
   - Gradle

2. 설치 및 실행:
```bash
git clone https://github.com/lukehemmin/LukeVanillaAPI.git
cd LukeVanillaAPI
./gradlew bootRun
```

3. 데이터베이스 설정:
   - `application.properties`에서 데이터베이스 연결 정보 구성

## 플러그인 연동 가이드

LukeVanilla 플러그인과 API 서버를 연동하려면:

1. 플러그인 설정 파일(`config.yml`)에서 API 설정 활성화:
```yaml
api:
  enabled: true
  baseUrl: "http://your-api-server:8080"
  timeout: 10000
```

2. 플러그인이 API 클라이언트를 통해 API 서버와 통신하도록 구성됩니다.

## 라이선스

MIT License - 자세한 내용은 LICENSE 파일을 참조하세요.

## 기여

기여를 원하시면 이슈를 등록하거나 풀 리퀘스트를 제출해 주세요.