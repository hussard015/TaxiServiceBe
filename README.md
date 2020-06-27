# TaxiServiceBe
- 택시 배차 앱 API 서버

# 실행 방법
- ./gradlew build
- java -jar ./build/libs/taxiservicebe-0.0.1-SNAPSHOT.jar

# Depedency 
- Spring Boot 2.3.1
- Spring Jpa
- Spring Security
- Spring Rest Docs
- Sqlite

# 프로젝트 설명
- Spring Security와 JWT를 사용하여 인증을 구현하였습니다.
- Spring Rest Docs를 사용하여 API 문서를 관리합니다.
- 테스트 코드를 작성하였습니다.

# Rest API document
서버 실행 후, http://localhost:8080/docs/index.html 에서 확인

# ERD

# 개발 도중 이슈
- Spring Jpa ddl-auto 로 테이블을 자동 생성하게 되면, Sqlite는 FK를 설정해주지 못함. 그래서 테이블을 스크립트로 작성하였음.
- Spring Jpa로 Sqlite를 사용하면, 트랜잭션기능을 전혀 못쓰게 되는 것 같음. Sqlite가 디비 접근시 커넥션하나만 허용해서 그런듯.
- OnetoOne 관계에서 쿼리실행 시, N + 1 문제 발생. Sqlite에서 FetchType Lazy를 못해서 개선 불가능함.
