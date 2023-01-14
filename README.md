# 2023-karrot-challenge
넘블 당근마켓 백엔드 클론코딩 챌린지를 진행하며 만든 결과물 (2022.12.19-2023.01.15)

## ERD 다이어그램

![논리적 설계](https://user-images.githubusercontent.com/78461009/212487727-e456e152-5df6-4c6c-acce-2572e07491ec.png)


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

### 6. 회고록
Blog : https://velog.io/@xiu0327/%EB%8B%B9%EA%B7%BC%EB%A7%88%EC%BC%93-%EC%B1%8C%EB%A6%B0%EC%A7%80-%ED%9A%8C%EA%B3%A0
