
## Application 실행하기
어떤 환경에서든 **'반드시 실행 가능'** 해야 하며 데이터베이스도 적절히 초기화 되어야 하므로 컨티이터너 이미지로 빌드하여 실행하는 방법을 제안드립니다.

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
- 다음의 jwt 를 이용하여 Swagger 문서의 Authorize 로 활용함
    - userId[1]: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlzcyI6IkFQSS1TZXJ2ZXIiLCJpYXQiOjE3MjgzOTc2ODQsImV4cCI6MTczMTMwMjQ4NH0.g12BIdABwzg4hTBm38_ekPv04GxpDATvBIPqPYvGm9k`
    - userId[2]: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjIsImlzcyI6IkFQSS1TZXJ2ZXIiLCJpYXQiOjE3MjgzOTc3NzgsImV4cCI6MTczMTMwMjU3OH0.Q0wkZGqTaFYGohJGw4nVDPFpPrCNtTwlyxCp_iXz7Zs`
    - userId[3]: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjMsImlzcyI6IkFQSS1TZXJ2ZXIiLCJpYXQiOjE3MjgzOTc3OTQsImV4cCI6MTczMTMwMjU5NH0.kZUaNRvWK9xNu4lV2TKFTgLnn-N1GY7j2Pa0fSbsLsA`
- 혹은 직접 유저 목록 조회 API (GET-https://localhost/users) 통해서 유저 이메일, 비밀번호를 활용하여 (POST-http://localhost/tokens/issues) 를 통해 jwt 를 발급하여 활용함
- 발급된 jwt 에 Bearer Prefix 를 붙여서 Swagger 문서의 Authorize 로 활용함


---
## Application 구성
- 프레임워크: SpringBoot 2.7.18
- 언어: Java 17
- DB : MySQL 8, Spring Data JPA, QueryDSL
- jwt: jjwt 라이브러리를 이용해 구현

---
## 테스트 구성
- Integration Test : Spring Bean 을 모두 로드하여 성공적으로 기능이 동작하는지 점검 
- Slice Test : 일부 Bean 을 로드하여 성공적인 기능 동작, 예외 사항을 점검
- Unit Test : POJO 에 대한 테스트 혹은 서비스 레이어에 Mock 을 주입하여 비즈니스 로직을 점검 

---
## 가산 요소
가산요소를 충족하기 위해 다음의 노력을 기울였습니다.

- 모던 자바 사용
  - Records 를 이용한 ResponseDTO 구성
  - Stream API 활용 
  - Optional 활용 (Repository 계층 및 테스트코드)
  - switch 표현식 활용 (RoomQueryPageService)
  - 텍스트 블록 활용 (RoomRepository 의 JPQL)
  - 로컬 변수 타입 추론 var 활용 (RoomQueryController)
</br></br>
- 공통 오류 처리
  - 공통 오류 코드 ErrorCode
  - 커스텀 예외 클래스 ApplicationException
  - ApplicationAdvice 를 이용한 공통 오류 처리
</br></br>
- Spring 추상화 사용
  - AOP 를 이용한 jwt 검증 (com.tsjeong.brokerage.aop.annotation.*)
</br></br>
- 테스트 코드 작성
  - 통합 테스트
  - 슬라이스 테스트
  - 단위 테스트
</br></br>
- API 문서 작성
  - OpenAPI, SwaggerUI 를 이용한 API 문서화