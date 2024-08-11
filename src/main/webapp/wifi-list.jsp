<%@ page import="com.zb_assignment.public_wifi_info.DAO.WifiDAO" %>
<%@ page import="com.zb_assignment.public_wifi_info.Entity.Wifi" %>
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

        tr:nth-child(even){background-color: #f2f2f2}

        th {
            background-color: #04AA6D;
            color: white;
        }
    </style>

    <script>
        function getLocation() {
            if(navigator.geolocation) {
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
            switch(error.code) {
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
            const lat = document.getElementById("lat").value;
            const lnt = document.getElementById("lnt").value;

            const params = new URLSearchParams({ lat, lnt });

            fetch('fetchWifiInfo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: params.toString()
            })
            // .then(response => response.text())
            // .then(data => alert(data))
            // .catch(error => console.error('Error:', error));

            window.location.href = 'wifi-list.jsp';
        }
    </script>

    <title>와이파이 정보 구하기</title>
</head>

<body>
<h1><%= "와이파이 정보 구하기" %></h1>
<br/>
<div class="spacing">
    <a href="index.jsp">홈</a> |
    <a href="history-list.jsp">위치 히스토리 목록</a> |
    <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
</div>

<div class="spacing">
    <label>LAT:</label>
    <input type="text" id="lat" placeholder="0.0"> ,
    <label>LNT:</label>
    <input type="text" id="lnt" placeholder="0.0">
    <button onclick="getLocation()">위치 가져오기</button>
    <button onclick="fetchWifiInfo()">와이파이 정보 가져오기</button>
</div>

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
        <th>망 종류</th>
        <th>설치년도</th>
        <th>실내외구분</th>
        <th>WIFI 접속환경</th>
        <th>X좌표</th>
        <th>Y좌표</th>
        <th>작업일자</th>
    </tr>
    <%
        WifiDAO wifiDAO = new WifiDAO();
        List<Wifi> dataList = wifiDAO.getAllWifi();

        for (Wifi wifi : dataList) {
    %>
    <tr>
        <td><%= 0 %></td>
        <td><%= wifi.getMGR_NO() %></td>
        <td><%= wifi.getWRDOFC() %></td>
        <td><%= wifi.getMAIN_NM() %></td>
        <td><%= wifi.getADRES1() %></td>
        <td><%= wifi.getADRES2() %></td>
        <td><%= wifi.getINSTL_FLOOR() %></td>
        <td><%= wifi.getINSTL_TY() %></td>
        <td><%= wifi.getINSTL_MBY() %></td>
        <td><%= wifi.getSVC_SE() %></td>
        <td><%= wifi.getCMCWR() %></td>
        <td><%= wifi.getCNSTC_YEAR() %></td>
        <td><%= wifi.getINOUT_DOOR() %></td>
        <td><%= wifi.getREMARS3() %></td>
        <td><%= wifi.getLAT() %></td>
        <td><%= wifi.getLNT() %></td>
        <td><%= wifi.getWORK_DTTM() %></td>
    </tr>
    <%
        }
    %>
</table>

</body>

</html>