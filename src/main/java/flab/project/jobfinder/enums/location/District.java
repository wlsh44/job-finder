package flab.project.jobfinder.enums.location;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum District {
    SEOUL("서울"),
    GANGNAM("강남구"),
    GANGDONG("강동구"),
    GANGBUK("강북구"),
    GANGSEO("강서구"),
    GWANAK("관악구"),
    GWANGJIN("광진구"),
    GURO("구로구"),
    GEUMCHEON("금천구"),
    NOWON("노원구"),
    DOBONG("도봉구"),
    DONGDAEMUN("동대문구"),
    DONGJAK("동작구"),
    MAPO("마포구"),
    SEODAEMUN("서대문구"),
    SEOCHO("서초구"),
    SEODONG("성동구"),
    SEONGBUK("성북구"),
    SONGPA("송파구"),
    YANGCHEON("양천구"),
    YEONGDEUNGPO("영등포구"),
    YONGSAN("용산구"),
    EUNPYEONG("은평구"),
    JONGNO("종로구"),
    JUNG("중구"),
    JUNGRANG("중랑구"),
    GYEONGGI("경기"),
    BUNDANG("분당구"),
    INCHEON("인천"),
    DAEJEON("대전"),
    SEJONG("세종"),
    CHUNGNAM("충남"),
    CHUNGBUK("충북"),
    GWANGJU("광주"),
    JEONNAM("전남"),
    JEONBUK("전북"),
    DAEGU("대구"),
    GYEONGBUK("경북"),
    BUSAN("부산"),
    ULSAN("울산"),
    GYEONGNAM("경남"),
    GANGWON("강원"),
    JEJU("제주");

    private final String district;

    District(String district) {
        this.district = district;
    }

    public String district() {
        return district;
    }

    private final static Map<String, String> map = Stream.of(values())
            .collect(Collectors.toUnmodifiableMap(District::district, District::name));

    public static Map<String, String> getDistrictMap() {
        return map;
    }

}
