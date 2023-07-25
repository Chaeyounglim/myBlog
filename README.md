# myBlog
</br>
태선 튜터님의 코드와 강의를 통해 코드 리팩토링 완료 <br/>
참조 REPO : https://github.com/thesun4sky/spring-blog/blob/lv4/﻿
</br>
</br>
</br>

## 기능 사항들

### 1. 핵심 기능
1. JWT 토큰을 이용한 로그인 및 회원가입 기능
2. REST ful한 API
3. 게시글 작성 및 수정, 삭제
4. 댓글 작성 및 수정, 삭제
5. 게시글 및 댓글 좋아요 기능
6. JWT 토큰을 검증하여 작성, 수정, 삭제 접근 제어
</br>
</br>
</br>
</br>


### 2. 기능 요구사항
1. 회원 가입 API
    - username, password를 Client에서 전달받기
    - username은  `최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)`로 구성되어야 한다.
    - password는  `최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자`로 구성되어야 한다.
    - DB에 중복된 username이 없다면 회원을 저장하고 Client 로 성공했다는 메시지, 상태코드 반환하기
    - 회원 권한 부여하기 (ADMIN, USER) - ADMIN 회원은 모든 게시글, 댓글 수정 / 삭제 가능
</br>

2. 로그인 API
    - username, password를 Client에서 전달받기
    - DB에서 username을 사용하여 저장된 회원의 유무를 확인하고 있다면 password 비교하기
    - 로그인 성공 시, 로그인에 성공한 유저의 정보와 JWT를 활용하여 토큰을 발급하고, 
    발급한 토큰을 Header에 추가하고 성공했다는 메시지, 상태코드 와 함께 Client에 반환하기
</br>

3. 전체 게시글 목록 조회 API
    - 제목, 작성자명(username), 작성 내용, 작성 날짜를 조회하기
    - 작성 날짜 기준 내림차순으로 정렬하기
    - 각각의 게시글에 등록된 모든 댓글을 게시글과 같이 Client에 반환하기
    - 댓글은 작성 날짜 기준 내림차순으로 정렬하기
    - 게시글/댓글에 ‘좋아요’ 개수도 함께 반환하기
</br>

4. 선택한 게시글 조회 API
    - 선택한 게시글의 제목, 작성자명(username), 작성 날짜, 작성 내용을 조회하기 
    (검색 기능이 아닙니다. 간단한 게시글 조회만 구현해주세요.)
    - 선택한 게시글에 등록된 모든 댓글을 선택한 게시글과 같이 Client에 반환하기
    - 댓글은 작성 날짜 기준 내림차순으로 정렬하기
    - 게시글/댓글에 ‘좋아요’ 개수도 함께 반환하기
</br>

5. 게시글 작성 API
    - 토큰을 검사하여, 유효한 토큰일 경우에만 게시글 작성 능  ⇒ Spring Security 를 사용하여 토큰 검사 및 인증하기!
    - 제목, 작성자명(username), 작성 내용을 저장하고
    - 저장된 게시글을 Client 로 반환하기
</br>

6. 선택한 게시글 수정 API
    - 토큰을 검사한 후, 유효한 토큰이면서 해당 사용자가 작성한 게시글만 수정 가능  ⇒ Spring Security 를 사용하여 토큰 검사 및 인증하기!
    - 제목, 작성 내용을 수정하고 수정된 게시글을 Client 로 반환하기
    - 게시글에 ‘좋아요’ 개수도 함께 반환하기
</br> 

7. 선택한 게시글 삭제 API
    - 토큰을 검사한 후, 유요한 토큰이면서 해당 사용자가 작성한 게시글만 삭제 가능 ⇒ Spring Security 를 사용하여 토큰 검사 및 인증하기!
    - 선택한 게시글을 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
</br>

8. 댓글 작성 API
    - 토큰을 검사하여, 유요한 토큰일 경우에만 댓글 작성 가능 ⇒ Spring Security 를 사용하여 토큰 검사 및 인증하기!
    - 선택한 게시글의 DB 저장 유무를 확인하기
    - 선택한 게시글이 있다면 댓글을 등록하고 등록된 댓글 반환하기
</br>

9. 댓글 수정 API
    - 토큰에 검사한 후, 유요한 토큰이면서 해당 사용자가 작성한 댓글만 수정 가능  ⇒ Spring Security 를 사용하여 토큰 검사 및 인증하기!
    - 선택한 댓글의 DB 저장 유무를 확인하기
    - 선택한 댓글이 있다면 댓글 수정하고 수정된 댓글 반환하기
    - 댓글에 ‘좋아요’ 개수도 함께 반환하기
</br>

10. 댓글 삭제 API
    - 토큰을 검사한 후, 유요한 토큰이면서 해당 사용자가 작성한 댓글만 삭제 가능 ⇒ Spring Security 를 사용하여 토큰 검사 및 인증하기!
    - 선택한 댓글의 DB 저장 유무를 확인하기
    - 선택한 댓글이 있다면 댓글 삭제하고 Client 로 성공했다는 메시지, 상태코드 반환하기
</br>

11. 게시글 좋아요 API
    - 사용자는 선택한 게시글에 ‘좋아요’를 할 수 있습니다.
    - 사용자가 이미 ‘좋아요’한 게시글에 다시 ‘좋아요’ 요청을 하면 ‘좋아요’를 했던 기록이 취소됩니다.
    - 요청이 성공하면 Client 로 성공했다는 메시지, 상태코드 반환하기
</br>

12. 댓글 좋아요 API
    - 사용자는 선택한 댓글에 ‘좋아요’를 할 수 있습니다.
    - 사용자가 이미 ‘좋아요’한 댓글에 다시 ‘좋아요’ 요청을 하면 ‘좋아요’를 했던 기록이 취소됩니다.
    - 요청이 성공하면 Client 로 성공했다는 메시지, 상태코드 반환하기
</br>

13. 예외처리
    - 아래 예외처리를 AOP 를 활용하여 구현하기
    - 토큰이 필요한 API 요청에서 토큰을 전달하지 않았거나 정상 토큰이 아닐 때는 "토큰이 유효하지 않습니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 토큰이 있고, 유효한 토큰이지만 해당 사용자가 작성한 게시글/댓글이 아닌 경우에는 “작성자만 삭제/수정할 수 있습니다.”라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - DB에 이미 존재하는 username으로 회원가입을 요청한 경우 "중복된 username 입니다." 라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 로그인 시, 전달된 username과 password 중 맞지 않는 정보가 있다면 "회원을 찾을 수 없습니다."라는 에러메시지와 statusCode: 400을 Client에 반환하기
    - 회원가입 시 username과 password의 구성이 알맞지 않으면 에러메시지와 statusCode: 400을 Client에 반환하기
</br>
</br>
</br>
</br>
</br>


## Entity Relationship Diagram
![image](https://github.com/Chaeyounglim/myBlog/assets/55676554/6b2f3677-6524-408b-9f12-004fe15a658a)
</br>
</br>
</br>
</br>
</br>

## DataBase Table Structure
![image](https://github.com/Chaeyounglim/myBlog/assets/55676554/51ab271d-0410-423e-87a8-225994753620)

</br>
</br>
</br>
</br>
</br>


## RESTful API 명세서
![image](https://github.com/Chaeyounglim/myBlog/assets/55676554/773c36fc-fed5-4327-bdf8-c1808015886d)
![image](https://github.com/Chaeyounglim/myBlog/assets/55676554/77853a37-41ab-4811-869a-bba6ee8cb10f)
![image](https://github.com/Chaeyounglim/myBlog/assets/55676554/36775aff-45ec-4f82-b6e5-c7e4c7c15ce2)
![image](https://github.com/Chaeyounglim/myBlog/assets/55676554/5fd3048a-1b0a-4183-bafe-633f809aba11)
![image](https://github.com/Chaeyounglim/myBlog/assets/55676554/98adc6db-7239-4462-8335-efbd89f94f16)
![image](https://github.com/Chaeyounglim/myBlog/assets/55676554/e9837b8b-f6cf-4d2a-905e-ad6c95847f26)



</br>
</br>
</br>
</br>
</br>

## API 기능별 명세 : Notion Link

[회원가입](https://www.notion.so/007596dd315d4400a057d0e9cd59a46b?pvs=4)

[로그인](https://www.notion.so/d9507ae99098411a8229ebd49ab9aa8f?pvs=21)

[프로필 조회](https://www.notion.so/bf0fe25ba3a2498c8313a34bf5fca847?pvs=21)

[프로필 수정](https://www.notion.so/45d5346e71f445799e7a59792082f93a?pvs=21)

[전체 게시글 조회](https://www.notion.so/63eb8d4b3a514d98a24b6cc611f113d7?pvs=21)

[선택 게시글 조회](https://www.notion.so/147ff7bca6c4474cb7bbd06be0cb6879?pvs=21)

[게시글 작성](https://www.notion.so/4cc433e87abe44208ca8984896cb0570?pvs=21)

[게시글 수정](https://www.notion.so/782ba354c74e4042b870480cc6f3f64c?pvs=21)

[게시글 삭제](https://www.notion.so/ca639f1a28344e97bacdf5b8e39f6f1b?pvs=21)

[댓글 작성](https://www.notion.so/4dd95199b10b405f8004f7bf5542ee8e?pvs=21)

[댓글 수정](https://www.notion.so/e79c84e903ee4de299357145cde75683?pvs=21)

[댓글 삭제](https://www.notion.so/b9ec2a8664504a7ab03d02a34377d7f5?pvs=21)

[게시글 좋아요 추가](https://www.notion.so/fd6fc018d7d04bdf82a1906fefb7ec8a?pvs=21)

[게시글 좋아요 감소](https://www.notion.so/e378ad9ccf0a42bb835c4bd706d066f6?pvs=21)

[댓글 좋아요 추가](https://www.notion.so/2b29597462414561856adac9dfa75396?pvs=21)

[댓글 좋아요 감소](https://www.notion.so/1776729bf42d441787808c325c3c6756?pvs=21)
