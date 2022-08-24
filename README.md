# 목차

1. [프로젝트 이름](#프로젝트-이름)
2. [프로젝트 설명](#프로젝트-설명)
3. [사용 기술](#사용-기술)
4. [배포 방식](#배포-방식)
5. [실행](#실행)

# 프로젝트 이름

job-finder

# 프로젝트 설명

구인 게시글이 플랫폼별로 다르게 올라옴

취업하려는 사람이 한 눈에 원하는 조건에 맞는 정보를 전부 볼 수 없음

→ 크롤링과 파싱을 통해 등록된 여러 플랫폼의 구직 게시글을 하나로 통합하여 ~~파일로 저장~~ 볼 수 있는 웹 서비스 구현

(로켓펀치, 잡플래닛 등)

→ 간단하게 만들 수 있는 계정을 추가하고 원하는 게시글을 북마크하고 여러 태그를 추가할 수 있음

# 사용 기술

사용 언어
- java 17

프레임워크
- spring, spring boot

데이터베이스
- Mysql

기타 라이브러리
- Junit5, Jsoup, JPA, Thymeleaef 등

# 배포 방식

1. aws, Jenkins, githook을 이용한 CI/CD
2. Docker, Docker hub를 이용한 컨테이너 배포


# 실행

### 데이터베이스 세팅

- `job_finder` 테이블 생성 -> src/main/java/resources/sql/ddl.sql 실행
- application.properties에 맞게 user, password 설정

### 도커 실행

```
docker login
docker pull k87913j/job-finder
docker-compose up -d
```
