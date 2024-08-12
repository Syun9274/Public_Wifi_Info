<%@ page import="com.zb_assignment.public_wifi_info.dao.HistoryDAO" %>
<%@ page import="com.zb_assignment.public_wifi_info.entity.History" %>
<%@ page import="java.util.List" %>
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

    <script>
        function deleteHistory(id) {
            const params = new URLSearchParams({ id: id });

            fetch('deleteHistory', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: params.toString()
            })

            window.location.href = 'history-list.jsp';
        }
    </script>

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
        <th>LAT</th>
        <th>LNT</th>
        <th>조회일자</th>
        <th>비고</th>
    </tr>

    <%
        HistoryDAO historyDAO = new HistoryDAO();
        List<History> dataList = historyDAO.getAllHistory();

        for (History history : dataList) {
    %>
    <tr>
        <td><%= history.getId() %></td>
        <td><%= history.getLat() %></td>
        <td><%= history.getLnt() %></td>
        <td><%= history.getDate() %></td>
        <td class="center">
            <button onclick="deleteHistory(<%= history.getId() %>)">삭제</button>
        </td>
    </tr>
    <%
        }
    %>

</table>

</body>

</html>
