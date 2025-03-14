package com.zb_assignment.public_wifi_info.component;

public class DistanceCalculator {

    // 지구 반지름 (단위: km)
    private static final double EARTH_RADIUS = 6371.0;

    /**
     * Haversine 공식을 이용하여 두 지점 사이의 거리를 계산
     * @param lat1 첫 번째 지점의 위도
     * @param lnt1 첫 번째 지점의 경도
     * @param lat2 두 번째 지점의 위도
     * @param lnt2 두 번째 지점의 경도
     * @return 두 지점 사이의 거리 (단위: km)
     */
    public static double calculateDistance(double lat1, double lnt1, double lat2, double lnt2) {

        // 위치 정보가 없다면 즉시 종료
        if (lat1 == 0 || lnt1 == 0 || lat2 == 0 || lnt2 == 0) {
            return 0.0;
        }

        // 위도, 경도를 라디안 단위로 변환
        double latRad1 = Math.toRadians(lat1);
        double lonRad1 = Math.toRadians(lnt1);
        double latRad2 = Math.toRadians(lat2);
        double lonRad2 = Math.toRadians(lnt2);

        // 위도 및 경도 차이 계산
        double deltaLat = latRad2 - latRad1;
        double deltaLon = lonRad2 - lonRad1;

        // Haversine 공식 적용
        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(latRad1) * Math.cos(latRad2) * Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 거리 계산
        return EARTH_RADIUS * c;
    }

    public static void main(String[] args) {
        // 서울(서울시청) ↔ 부산(부산시청) 거리 계산 예제
        double distance = calculateDistance(37.5665, 126.9780, 35.1796, 129.0756);
        System.out.printf("두 지점 사이의 거리: %.2f km%n", distance);
    }
}
