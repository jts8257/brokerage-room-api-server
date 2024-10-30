
## Application 실행하기
어떤 환경에서든 Docker 가 설치되어있고, 네트워크 접근이 가능하다면 실행 가능하도록 컨테이너호 하여 실행합니다.

1. Docker 설치 ['가이드 링크'](https://docs.docker.com/engine/install/)
2. 이미지 빌드 및 컨테이너 실행
* 주의사항: 이 컨테이너의 사용 포트는 **8080** 입니다. 기존에 사용중인 포트가 있다면 SpringBootApplication 이 실행되지 못할 수 있습니다.
```bash
# 최초 이미지 빌드와 이미지에 패키징된 어플리케이션 실행을 함께 하려면
$> docker compose -f ./package/docker-compose.yml up --build -d

# 이미 빌드된 이미지를 실행하려면
$> docker compose -f ./package/docker-compose.yml up -d
```
3. 어플리케이션 중지/삭제
```bash 
# 컨테이너 중지
$> docker compose -f ./package/docker-compose.yml stop 
# 컨테이너 삭제
$> docker compose -f ./package/docker-compose.yml down
```
---
## API 문서
**Docker Container 를 시작한 상태**에서 http://localhost:8080/swagger-ui/index.html

### 활용
- 직접 유저 목록 조회 API (GET-https://localhost/users) 통해서 유저 이메일, 비밀번호를 활용하여 (POST-http://localhost/tokens/issues) 를 통해 jwt 를 발급하여 활용함
- 발급된 jwt 에 Bearer Prefix 를 붙여서 Swagger 문서의 Authorize 로 활용함


---
## Application 구성
- 프레임워크: SpringBoot 3.3.6
- 언어: Java 21
- DB : MySQL 8, Spring Data JPA, QueryDSL
- jwt: jjwt 라이브러리를 이용해 구현

### 프로젝트 구조
```bash
├── BrokerageApplication.java
├── aop # AOP 구현
├── config # JPA, JWT, Swagger, CORS 설정
├── controller # 컨틀롤러
├── dto # Request, Response, 공통, 에러 DTO, DTO Mapper
├── entity # 내방, 유저 엔티티
├── exception # 공통 예외 처리, 에러 코드, 커스텀 예외
├── repsoitory 
└── service # 로직 구현
```
### 테스트 구성
```bash
├── integration #Spring Bean 을 모두 로드하여 성공적으로 기능이 동작하는지 점검  
├── slice # 일부 Bean 을 로드하여 성공적인 기능 동작, 예외 사항을 점검
└── unit # POJO 에 대한 테스트, 서비스 레이어 로직에 집중한 테스트 
```