## [4Week] 김민재

### 미션 요구사항 분석 & 체크리스트

---

## 체크리스트

---

- [x]  JWT 로그인 구현(POST /api/v1/member/login)
- [x]  내 도서 리스트 구현(GET /api/v1/myBooks)
- [x]  내 도서 상세정보 구현(GET /api/v1/myBooks/{id})
- [x]  로그인 한 회원의 정보 구현(GET /api/v1/member/me)
- [x]  Srping Doc 으로 API 문서화(크롬 /swagger-ui/index.html )
    - [x]  문서 꾸미기, 권한 버튼 넣기

### 4주차 미션 요약

---

### [참고한 자료 및 사이트]

- JWT 강의
    - [https://wiken.io/ken/10698](https://wiken.io/ken/10698)
- [https://www.baeldung.com/jackson-name-of-property](https://www.baeldung.com/jackson-name-of-property)

### **[특이사항]**

구현 과정에서 아쉬웠던 점 / 궁금했던 점을 정리합니다.

- 배치로 정산 데이터 생성 후 수동으로 생성하거나 수동으로 생성 후 배치로 생성하면 생성되었던 정산데이터가 또 중복 생성되는 문제
- Json화 할때 컬럼명 그대로 json화 된다.
    
    <img width="551" alt="스크린샷 2022-11-09 오후 5 10 07" src="https://user-images.githubusercontent.com/97084128/200774394-91473bce-6464-427f-90da-eeea89227c78.png">

    
    - JsonProperty(”…”) 사용 →
        
        <img width="542" alt="스크린샷 2022-11-09 오후 5 10 34" src="https://user-images.githubusercontent.com/97084128/200774472-e4bbb968-3b04-4cc0-b141-c76477de688b.png">

        
    - json으로 변환시 이름 설정 가능

### [Refactoring]

- 테스트 코드
- System.out.println이나 console.log와 같은 값 확인용 코드 제거
