# 2023-karrot-challenge
넘블 당근마켓 백엔드 클론코딩 챌린지를 진행하며 만든 결과물 (2022.12.19-2023.01.15)

Page : http://35.244.45.103:8080/

Blog : https://velog.io/@xiu0327/%EB%8B%B9%EA%B7%BC%EB%A7%88%EC%BC%93-%EC%B1%8C%EB%A6%B0%EC%A7%80-%ED%9A%8C%EA%B3%A0

#### 프로젝트 개발 환경
- 운영체제 : MacOs (M1 pro)
- 통합개발환경(IDE) : IntelliJ
- JDK 버전 : JDK 11
- 스프링 부트 버전 : 2.7.7
- 데이터 베이스 : MySQL
- 빌드 툴 : Gradle
- 관리 툴 : Git, GitHub


#### 기술 스택
- 프론트엔드 : HTML, CSS, Thymeleaf
- 백엔드 : Spring, Springboot, Spring Security
- 배포 : GKE(Google Kubernetes Engine), Docker
- 데이터 베이스 : RDS
- 이미지 저장 : S3 

## Docker 실행
~~~
docker pull xiu0327/karrot
docker run -d --name karrot-container -p 8080:8080 xiu0327/karrot
~~~

## ERD 다이어그램

![논리적 설계](https://user-images.githubusercontent.com/78461009/212488771-82bd5b95-b83e-45d5-89ff-8df392410ead.png)


## 엔티티 관계도

<img width="872" alt="엔티티 관계도" src="https://user-images.githubusercontent.com/78461009/212487731-11395a72-81ad-479a-81ea-408e1b5f585d.png">

## 기능
### 1. 회원가입 및 로그인 기능
![회원가입 및 로그인 기능](https://user-images.githubusercontent.com/78461009/212487742-5e2bb83b-76c4-4f61-896c-bf976b21a8ff.gif)


- 스프링 시큐리티를 이용하여 로그인 구현
- 스프링 시큐리티의 Remember-me를 활용하여 로그인 세션 유지
- 이메일, 비밀번호, 이름, 핸드폰번호, 닉네임 입력 시 회원가입 진행

### 2. 상품 등록 기능
![상품 등록 기능](https://user-images.githubusercontent.com/78461009/212487756-24d4af2e-f76c-4079-a042-eca4547daa62.gif)


- 상품 사진은 S3에 저장 후, 이미지 URL을 DB에 저장
- 제목, 카테고리, 가격, 본문을 입력 받음

### 3. 상품 상세 페이지
![상품 상세페이지](https://user-images.githubusercontent.com/78461009/212487765-c8dd34f3-b74a-457e-bbe4-7087cbb60f88.gif)


- 게시시간 치환 : 1시간 전 = n분 전, 하루 전 = n 시간 전, 1년 전 = n 달 전, 1년 후 = n년 후
- 시간을 다룰 땐, Duration 이용. 날짜를 다룰 땐, Period 이용.

### 4. 마이페이지
![마이 페이지](https://user-images.githubusercontent.com/78461009/212487769-e0c86b83-15c9-4693-a696-2f13b2816b26.gif)


- 프로필 수정 가능
- 상품 상태별(판매중/거래완료) 판매 상품 목록 조회 가능
- 판매 내역에서 상품 상태 수정 가능
- 상품 상세 페이지에서 상품 상태 변경 가능
- 관심 목록 조회(내가 하트를 누른 판매 상품 목록)

### 5. 1:1 채팅 기능
![채팅 기능](https://user-images.githubusercontent.com/78461009/212487775-886d3a2a-5f55-465a-82d7-3f2ebb7f500b.gif)


- 스프링 내장 메시지 브로커를 통해 웹소켓 활용
- 추후 마지막 대화 내용 구현 예정
- 구매자 = 채팅하기 / 판매자 = 상품과 관련된 채팅 목록 조회 가능

## 유지보수
### 1. 리팩토링 진행
- 1월 21일 : 1차 리팩토링 완료
  - templates를 기능별로 디렉토리 생성
  - Member, Product의 Service와 Repository 인터페이스 삭제
    - 모든 리팩토링과 테스트를 끝낸 후, 앞으로 서비스를 업데이트할 때 꼭 필요한 기능만 따로 모아 인터페이스를 생성할 예정
    - 무분별한 인터페이스 혹은 불필요한 인터페이스는 오히려 유지보수에 방해가 됨
    - 레포지토리 인터페이스는 필요성을 느끼지 못하여 추후 생성하지 않음
  - PresentTime을 TimeService로 변환 후, 시간 치환 함수를 TimeService로 옮김
    - 유사한 기능끼리 한 클래스에 모아두는 것이 좋다고 판단
  - Controller에서는 되도록 엔티티를 다루지 않는 방향으로 수정
    - 비즈니스 로직은 Service 단계에서 처리하도록 MemberService, ProductService 수정
    - 엔티티와 관련된 비지니스/조회 로직은 엔티티에서 해결하도록 수정
  - 트랜잭션 범위를 고려한 리팩토링 진행
    - 트랜잭션 범위를 고려하여 불필요한 조회가 발생하지 않도록 최대한 수정
- 1월 23일 : 2차 리팩토링 완료
  - Interest, Chatting의 Repository, Service 인터페이스 삭제
  - 1차 리팩토링과 유사하게 트랜잭션 범위를 효율적으로 사용하기 위해 Controller에 있는 비즈니스 로직을 Service로 옮김
  - 불필요한 메서드 삭제
- 1월 25일 : 3차 리팩토링 완료
  - 모든 Repository를 Spring jpa data로 전환
### 2. 기능 추가
- 1월 25일 : 마지막 대화 조회 기능 추가
  - 연관 관계 메서드에서 `chatRoom.getChats().add(this)`를 추가하면 insert 문에 왜 여러 개 날려지는지 이유 알아낼 것
  