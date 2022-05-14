package flab.project.jobfinder.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Location {
    SEOUL("서울", "I000", "서울특별시"),
    GANGNAM("강남구", "I010", "강남구"),
    GANGDONG("강동구", "I020", "강동구"),
    GANGBUK("강북구", "I030", "강북구"),
    GANGSEO("강서구", "I040", "강서구"),
    GWANAK("관악구", "I050", "관악구"),
    GWANGJIN("광진구", "I060", "광진구"),
    GURO("구로구", "I070", "구로구"),
    GEUMCHEON("금천구", "I080", "금천구"),
    NOWON("노원구", "I090", "노원구"),
    DOBONG("도봉구", "I100", "도봉구"),
    DONGDAEMUN("동대문구", "I110", "동대문구"),
    DONGJAK("동작구", "I120", "동작구"),
    MAPO("마포구", "I130", "마포구"),
    SEODAEMUN("서대문구", "I140", "서대문구"),
    SEOCHO("서초구", "I150", "서초구"),
    SEODONG("성동구", "I160", "성동구"),
    SEONGBUK("성북구", "I170", "성북구"),
    SONGPA("송파구", "I180", "송파구"),
    YANGCHEON("양천구", "I190", "양천구"),
    YEONGDEUNGPO("영등포구", "I200", "영등포구"),
    YONGSAN("용산구", "I210", "용산구"),
    EUNPYEONG("은평구", "I220", "은평구"),
    JONGNO("종로구", "I230", "종로구"),
    JUNG("중구", "I240", "중구"),
    JUNGRANG("중랑구", "I250", "중랑구"),
    GYEONGGI("경기", "B000", "경기도"),
    BUNDANG("분당구", "B150", "분당구"),
    INCHEON("인천", "K000", "인천"),
    DAEJEON("대전", "G000", "대전"),
    SEJONG("세종", "1000", "세종"),
    CHUNGNAM("충남", "O000", "충청남도"),
    CHUNGBUK("충북", "P000", "충청북도"),
    GWANGJU("광주", "E000", "광주"),
    JEONNAM("전남", "L000", "전라남도"),
    JEONBUK("전북", "M000", "전라북도"),
    DAEGU("대구", "F000", "대구"),
    GYEONGBUK("경북", "D000",  "경상북도"),
    BUSAN("부산", "H000", "부산"),
    ULSAN("울산", "J000", "울산"),
    GYEONGNAM("경남", "C000", "경상남도"),
    GANGWON("강원", "A000", "강원도"),
    JEJU("제주", "N000", "제주도");

    private final String district;
    private final String jobKoreaCode;
    private final String rocketPunchCode;

    private final static Map<String, String> map = Stream.of(values())
            .collect(Collectors.toUnmodifiableMap(Location::district, Location::name));

    Location(String district, String jobKoreaCode, String rocketPunchCode) {
        this.district = district;
        this.jobKoreaCode = jobKoreaCode;
        this.rocketPunchCode = rocketPunchCode;
    }

    public String district() {
        return district;
    }

    public String jobKoreaCode() {
        return jobKoreaCode;
    }

    public String rocketPunchCode() {
        return rocketPunchCode;
    }

    public static Map<String, String> getDistrictMap() {
        return map;
    }
}
