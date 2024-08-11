# Public Wifi Info
`Open API`를 이용한 `JSP` 프로젝트 만들기

[Open API](https://data.seoul.go.kr/dataList/OA-20883/S/1/datasetView.do)

## Stack

![Java](https://img.shields.io/badge/Java-red)
![JSP](https://img.shields.io/badge/JSP-orange)
![MySQL](https://img.shields.io/badge/MySQL-blue)
![Gradle](https://img.shields.io/badge/Gradle-brightgreen)
![Tomcat](https://img.shields.io/badge/Tomcat-grey)


---

### 진척도

- [x] tomcat 서버 연결
- [x] DB 연결
- [x] DB table 생성
- [x] Open API 연결 및 호출
- [x] Open API 호출로 불러온 값을 DB에 저장
- [x] UI 배치
- [x] DB 데이터를 화면에 띄워서 보여주기
- [x] 버튼 한번으로 현재 위치의 위도 경도 불러오기
- [ ] 위도 경도 값을 토대로 주변 wifi를 가까운 순서대로 정렬하기
- [x] 검색 history 데이터 삽입하기
- [x] history table 속 데이터 선택 삭제하기
- [x] history 데이터 삭제 후 자동 페이지 새로고침

### 발견된 문제점

``` java
1. request.setAttribute("dataList", dataList);
2. request.getRequestDispatcher("jspFile_name.jsp").forward(request, response);
```
1. 이 부분에 이유를 알 수 없는 오류가 있는지 `.jsp` 파일 측에서 `dataList`를 인식하지 못한다.
    - `javax.servlet.ServletException`에 따르면 위의 2번 코드에서 예외가 발생했다고 한다.
2. `load-wifi.jsp`에 접속하면 연동된 `LoadWifiServlet.java`가 무한루프에 빠진다.
   - 이때, OpenAPI 호출과 DB 데이터 삽입은 정상적으로 작동된다.