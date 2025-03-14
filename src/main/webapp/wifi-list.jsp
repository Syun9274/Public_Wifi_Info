<%@ page import="com.zb_assignment.public_wifi_info.dao.WifiDAO" %>
<%@ page import="com.zb_assignment.public_wifi_info.entity.Wifi" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
    <style>
        .spacing {
            margin-bottom: 10px;
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
            text-align: center;
            padding: 8px;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2
        }

        th {
            background-color: #04AA6D;
            color: white;
        }
    </style>

    <script>
        function getLocation() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(showPosition, showError);
            } else {
                alert("Geolocation not working")
            }
        }

        function showPosition(position) {
            const lat = position.coords.latitude;
            const lnt = position.coords.longitude;

            document.getElementById("lat").value = lat;
            document.getElementById("lnt").value = lnt;
        }

        function showError(error) {
            switch (error.code) {
                case error.PERMISSION_DENIED:
                    alert("사용자가 위치 정보 제공을 거부했습니다.");
                    break;
                case error.POSITION_UNAVAILABLE:
                    alert("위치 정보를 사용할 수 없습니다.");
                    break;
                case error.TIMEOUT:
                    alert("요청 시간이 초과되었습니다.");
                    break;
            }
        }

        function fetchWifiInfo() {
            let lat = document.getElementById("lat").value;
            let lnt = document.getElementById("lnt").value;

            // 빈 값일 경우 기본값 0.0 설정
            if (!lat) lat = "0.0";
            if (!lnt) lnt = "0.0";

            // URL에 위도, 경도를 포함하여 페이지 이동
            window.location.href = `wifi-list.jsp?page=1&lat=${lat}&lnt=${lnt}`;
        }
    </script>
    <title>와이파이 정보 구하기</title>
</head>

<%
    // 위도, 경도 변수를 먼저 선언
    double lat = 0.0;
    double lnt = 0.0;

    // request.getParameter() 값을 가져오고 빈 문자열 처리
    String latParam = request.getParameter("lat");
    String lntParam = request.getParameter("lnt");

    if (latParam != null && !latParam.trim().isEmpty()) {
        try {
            lat = Double.parseDouble(latParam);
        } catch (NumberFormatException e) {
            lat = 0.0; // 잘못된 값이 들어오면 기본값 설정
        }
    }

    if (lntParam != null && !lntParam.trim().isEmpty()) {
        try {
            lnt = Double.parseDouble(lntParam);
        } catch (NumberFormatException e) {
            lnt = 0.0; // 잘못된 값이 들어오면 기본값 설정
        }
    }
%>

<body>
<h1><%= "와이파이 정보 구하기" %>
</h1>
<br/>
<div class="spacing">
    <a href="index.jsp">홈</a> |
    <a href="history-list.jsp">위치 히스토리 목록</a> |
    <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
</div>

<div class="spacing">
    <label>LAT:</label>
    <input type="text" id="lat" placeholder="0.0" value="<%= lat %>">
    <label>LNT:</label>
    <input type="text" id="lnt" placeholder="0.0" value="<%= lnt %>">
    <button onclick="getLocation()">위치 가져오기</button>
    <button onclick="fetchWifiInfo()">와이파이 정보 가져오기</button>
</div>

<%
    int pageSize = 20; // 한 페이지당 표시할 데이터 개수
    int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;

    WifiDAO wifiDAO = new WifiDAO();
    List<Wifi> dataList;
    long totalWifiCount = wifiDAO.getWifiCount();

    // 위도, 경도 값이 0.0이면 기존 메서드 사용
    if (lat == 0.0 || lnt == 0.0) {
        dataList = wifiDAO.getAllWifi(currentPage, pageSize);
    } else {
        dataList = wifiDAO.getNearbyWifi(lat, lnt, currentPage, pageSize);
    }

    int totalPages = (int) Math.ceil((double) totalWifiCount / pageSize);
%>

<table>
    <tr>
        <th>거리(km)</th>
        <th>관리번호</th>
        <th>자치구</th>
        <th>와이파이명</th>
        <th>도로명주소</th>
        <th>상세주소</th>
        <th>설치위치(층)</th>
        <th>설치유형</th>
        <th>설치기관</th>
        <th>서비스구분</th>
        <th>실내외구분</th>
    </tr>
    <%
        for (Wifi wifi : dataList) {
    %>
    <tr>
        <td>
            <% if (lat != 0.0 && lnt != 0.0) { %>
                <%= String.format("%.2f", wifi.getDistance()) %>
            <% } else { %>
                -
            <% } %>
        </td>
        <td><%= wifi.getMGR_NO() %>
        </td>
        <td><%= wifi.getWRDOFC() %>
        </td>
        <td><%= wifi.getMAIN_NM() %>
        </td>
        <td><%= wifi.getADRES1() %>
        </td>
        <td><%= wifi.getADRES2() %>
        </td>
        <td><%= wifi.getINSTL_FLOOR() %>
        </td>
        <td><%= wifi.getINSTL_TY() %>
        </td>
        <td><%= wifi.getINSTL_MBY() %>
        </td>
        <td><%= wifi.getSVC_SE() %>
        </td>
        <td><%= wifi.getINOUT_DOOR() %>
        </td>
    </tr>
    <%
        }
    %>
</table>

<!-- 페이징 버튼 -->
<div class="pagination">
    <%
        int maxPageLinks = 5; // 화면에 표시할 최대 페이지 수
        int halfPageLinks = maxPageLinks / 2;
        int startPage = Math.max(1, currentPage - halfPageLinks);
        int endPage = Math.min(totalPages, startPage + maxPageLinks - 1);

        // 만약 마지막 페이지가 totalPages보다 작을 경우, 시작 페이지를 조정
        if (endPage - startPage < maxPageLinks - 1) {
            startPage = Math.max(1, endPage - maxPageLinks + 1);
        }

        // 현재 URL에 lat, lnt 값이 포함되도록 설정
        String queryParams = "&lat=" + lat + "&lnt=" + lnt;
    %>

    <% if (currentPage > 1) { %>
        <a href="?page=1<%= queryParams %>">&laquo; 처음</a>
        <a href="?page=<%= currentPage - 1 %><%= queryParams %>">&lt; 이전</a>
    <% } %>

    <% for (int i = startPage; i <= endPage; i++) { %>
        <a href="?page=<%= i %><%= queryParams %>" class="<%= (i == currentPage) ? "active" : "" %>"><%= i %></a>
    <% } %>

    <% if (currentPage < totalPages) { %>
        <a href="?page=<%= currentPage + 1 %><%= queryParams %>">다음 &gt;</a>
        <a href="?page=<%= totalPages %><%= queryParams %>">마지막 &raquo;</a>
    <% } %>
</div>

<!-- 스타일 추가 -->
<style>
    .pagination {
        margin-top: 20px;
        text-align: center;
    }

    .pagination a {
        display: inline-block;
        padding: 8px 12px;
        margin: 0 4px;
        border: 1px solid #ddd;
        text-decoration: none;
        color: #333;
    }

    .pagination a.active {
        background-color: #007bff;
        color: white;
        border: 1px solid #007bff;
    }
</style>

</body>

</html>