<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <head>
    <style>
      h1 {
        text-align: center;
        margin-bottom: 10px;
      }

      .spacing {
        text-align: center;
      }

    </style>

    <title>와이파이 정보 구하기</title>
  </head>
  <body>

  <h1>
    <c:out value= "${listTotalCount}"/>개의 WIFI 정보를 정상적으로 저장하였습니다.
  </h1>

  <div class="spacing">
    <a href="index.jsp">홈으로 가기</a>
  </div>

  </body>
</html>
