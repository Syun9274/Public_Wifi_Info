<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <style>
        .spacing {
            margin-bottom: 10px;
        }

        .center {
            text-align: center;
        }

        table {
            border-collapse: collapse;
            width: 100%;
        }

        th {
            border: 1px solid white;
            text-align: center;
            padding: 8px;
        }

        td {
            border: 1px solid gray;
            text-align: left;
            padding: 8px;
        }

        tr:nth-child(even){background-color: #f2f2f2}

        th {
            background-color: #04AA6D;
            color: white;
        }
    </style>

    <title>와이파이 정보 구하기</title>
</head>

<body>
<h1><%= "위치 히스토리 목록" %></h1>
<br/>
<div class="spacing">
    <a href="index.jsp">홈</a> |
    <a href="history-list.jsp">위치 히스토리 목록</a> |
    <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
</div>

<table>
    <tr>
        <th>ID</th>
        <th>X 좌표</th>
        <th>Y 좌표</th>
        <th>조회일자</th>
        <th>비고</th>
    </tr>

    <c:forEach var="history" items="${dataList}">
        <tr>
            <td>${history.id}</td>
            <td>${history.xPos}</td>
            <td>${history.yPos}</td>
            <td>${history.date}</td>
            <td class="center">
                <button onclick="deleteHistory(${history.id})">삭제</button>
            </td>
        </tr>
    </c:forEach>

</table>

</body>

</html>
