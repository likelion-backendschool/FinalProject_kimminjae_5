## [1Week] 김민재

### 미션 요구사항 분석 & 체크리스트

---

## 체크리스트

---

회원

- [x]  회원 엔티티 작성
- [x]  회원 관련 테스트 코드 작성
- [x]  회원가입 구현
    - [x]  회원가입시 가입축하 메일 발송
    - [x]  가입 직후는 일반 회원
    - [x]  별개로 authLevel 의 값이 7이면 관리자의 역할도 수행할 수 있다.
- [x]  로그인, 로그아웃 구현
    - [x]  시큐리티
- [x]  아이디 찾기 구현
    - [x]  이메일 도입
- [x]  비밀번호 찾기 구현
    - [x]  아이디와 이메일 이용
    - [x]  메일로 임시 비밀번호 발송
- [x]  회원 정보 수정 구현
    - [x]  작가 되기 구현
    - [x]  개인 정보 수정 구현
- [x]  비밀번호 변경 구현
- [ ]  프로필 페이지 구현
    - [x]  작가명 입력 후 작가 등록을 할 수 있다.
    - [ ]  작가 회원은 상품(책) 등록 가능

글

- [x]  글 리스트 페이지 구현
    - [x]  제목, 글, 해시태그 출력
    - [x]  해시태그 클릭시 관련 글들 노출
- [x]  글 작성 구현
    - [ ]  글은 최소 1000자 이상
    - [ ]  마크다운 적용
- [x]  글 수정 구현
- [x]  글 삭제 구현
    - [x]  confirm으로 한 번 더 삭제여부를 묻는다.
    - [x]  삭제 완료 후 리스트 페이지로 이동
- [ ]  글 상세화면 구현
    - [x]  제목, 내용, 해시태그 출력
    - [ ]  마크다운 적용

글 해시태그

- [ ]  최소 1개, 최대 10개의 해시태그 가능

### 1주차 미션 요약

---

**[접근 방법]**

체크리스트를 중심으로 각각의 기능을 구현하기 위해 어떤 생각을 했는지 정리합니다.

- 무엇에 중점을 두고 구현하였는지, 어떤 공식문서나 예제를 참고하여 개발하였는지 뿐만 아니라 미션을 진행하기 전 개인적으로 실습한 것도 포함하여 작성해주시기 바랍니다.
- 실제 개발 과정에서 목표하던 바가 무엇이었는지 작성해주시기 바랍니다.
- 구현 과정에 따라 어떤 결과물이 나오게 되었는지 최대한 상세하게 작성해주시기 바랍니다.

### [중점을 둔 것 & 목표]

- 사용자의 입장을 많이 고려했다.
- 내가 직접 사이트를 이용한다고 가정하고, 발생할 만한 예외 상황들을 많이 생각했다.
- 최대한 Controller에서는 Dto를 사용하고, Service에서 entity를 Dto로 변환하는 작업을 일임했다.
- 파일들을 hashtag, post, member, keyword 와 같이 정리하여 개발을 진행했다.
- 개발 목표
    - 사용자의 입장에서 사용하기에 불편하지 않도록 하는 것

### [참고한 자료 및 사이트]

- spring boot를 배울 때 참고했던 점프 투 스프링부트의 sbb프로젝트의 코드와 관련된 내 블로그 글을 활용했다.
    - [myblog](https://velog.io/@gimminjae/series/SBB-spring-boot-board%EC%A0%90%ED%94%84-%ED%88%AC-%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8)
    - DataNotFoundException
    - form_error
    - 타임리프 공통 템플릿 활용법
- 해커톤 팀프로젝트를 하며 정리했던 블로그글을 활용했다.
    - [myblog](https://velog.io/@gimminjae/series/%EC%97%AC%ED%96%89%EC%9D%84-DAMDA-%EB%A9%8B%EC%9F%81%EC%9D%B4%EC%82%AC%EC%9E%90%EC%B2%98%EB%9F%BC-%EB%B0%B1%EC%97%94%EB%93%9C-%EC%8A%A4%EC%BF%A8-1%EA%B8%B0-%ED%95%B4%EC%BB%A4%ED%86%A4)
- 아이디 찾기
    - [https://hianna.tistory.com/m/411](https://hianna.tistory.com/m/411)
    - [블로그의 ajax 정리 요약본](https://velog.io/@gimminjae/Ajax-jQuery)
- 메일 발송
    - [https://velog.io/@jyleedev/스프링부트-SMTP-Gmail-이용하여-이메일-전송-구현-임시-비밀번호-발송-기능](https://velog.io/@jyleedev/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-SMTP-Gmail-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%EC%9D%B4%EB%A9%94%EC%9D%BC-%EC%A0%84%EC%86%A1-%EA%B5%AC%ED%98%84-%EC%9E%84%EC%8B%9C-%EB%B9%84%EB%B0%80%EB%B2%88%ED%98%B8-%EB%B0%9C%EC%86%A1-%EA%B8%B0%EB%8A%A5)
    - [https://bamdule.tistory.com/m/238](https://bamdule.tistory.com/m/238)
    - [https://born2bedeveloper.tistory.com/m/14](https://born2bedeveloper.tistory.com/m/14)
- 임시 비밀번호 발급
    - 무작위 문자열 생성 [https://vmpo.tistory.com/m/125](https://vmpo.tistory.com/m/125)
- 글 작성시 미리보기
    - [https://hianna.tistory.com/m/411](https://hianna.tistory.com/m/411)
- UI 꾸미기
    - 테일윈드/데이지UI [https://daisyui.com/components/](https://daisyui.com/components/)
- 해시태그 구현
    - [https://www.youtube.com/watch?v=8UHQJ1uDECk](https://www.youtube.com/watch?v=8UHQJ1uDECk)
- 마크다운
    - [https://codingnojam.tistory.com/3](https://codingnojam.tistory.com/3)
    - [https://wiken.io/ken/9291](https://wiken.io/ken/9291)

### **[특이사항]**

구현 과정에서 아쉬웠던 점 / 궁금했던 점을 정리합니다.

- 추후 리팩토링 시, 어떤 부분을 추가적으로 진행하고 싶은지에 대해 구체적으로 작성해주시기 바랍니다.
    - 메일 처리 속도 개선
        - 현재 회원가입 후 축하메일 발송, 임시 비밀번호 발송 등의 작업에 약 2~3초의 시간이 걸리는데 2, 3초도 사용자에게는 긴 시간일 수 있으므로 메일 전송 속도 개선이 필요하다.
    - 테스트 코드 보충
        - 전체 코드량 대비 테스트 코드가 상대적으로 적다. 새로운 기능 추가나 코드의 변경이 발생할 시 코드를 효율적으로 관리하기 위해 테스트 코드를 더 촘촘히 작성하고 보충할 필요성이 있다.
    - 글 상세페이지 이슈
        - 작성, 수정 시 마크다운 파일이 렌더링되어 잘 저장되지만, 테일윈드 문법 때문인지 상세페이지에서 태그 적용이 안됨.
    

### [Refactoring]

- html, css, js 코드들에 코드를 쉽게 파악할 수 있도록 주석 추가, 코드 정리
- 불필요한 코드 검사 및 제거
- 보안상 좋지 않은 로직 변경
