
## Application 실행하기
이 API 서버가 어떠한 환경에서 실행될지 명시되지 않았으며, 데이터베이스 또한 적절히 초기화되어야 한다는 점에서 **'반드시 실행 가능해야 합니다'** 조건을 가장 잘 충족하는 방안은 컨테이너 기술을 활용하여 Spring Boot 애플리케이션과 데이터베이스를 이미지로 패키징하는 것이라고 판단하였습니다.

1. Docker 설치 ['가이드 링크'](https://docs.docker.com/engine/install/)
2. 이미지 빌드 및 컨테이너 실행
```bash
$> docker compose -f ./package/docker-compose.yml up --build -d
```
3. 어플리케이션 중지/삭제
```bash 
# 컨테이너 중지
$> docker compose -f ./package/docker-compose.yml stop 
# 컨테이너 삭제
$> docker compose -f ./package/docker-compose.yml down
```
