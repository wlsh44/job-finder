package flab.project.jobfinder.enums;

public enum Location {
    SEOUL("서울", "I000"),
    GANGNAM("강남구", "I010"),
    GANGDONG("강동구", "I020"),
    GANGBUK("강북구", "I030"),
    GANGSEO("강서구", "I040"),
    GWANAK("관악구", "I050"),
    GWANGJIN("광진구", "I060"),
    GURO("구로구", "I070"),
    GEUMCHEON("금천구", "I080"),
    NOWON("노원구", "I090"),
    DOBONG("도봉구", "I100"),
    DONGDAEMUN("동대문구", "I110"),
    DONGJAK("동작구", "I120"),
    MAPO("마포구", "I130"),
    SEODAEMUN("서대문구", "I140"),
    SEOCHO("서초구", "I150"),
    SEODONG("성동구", "I160"),
    SEONGBUK("성북구", "I170"),
    SONGPA("송파구", "I180"),
    YANGCHEON("양천구", "I190"),
    YEONGDEUNGPO("영등포구", "I200"),
    YONGSAN("용산구", "I210"),
    EUNPYEONG("은평구", "I220"),
    JONGNO("종로구", "I230"),
    JUNG("중구", "I240"),
    JUNGRANG("중랑구", "I250"),
    GYEONGGI("경기", "B000"),
    BUNDANG("분당구", "B150"),
    INCHEON("인천", "K000"),
    DAEJEON("대전", "G000"),
    SEJONG("세종", "1000"),
    CHUNGNAM("충남", "O000"),
    CHUNGBUK("충북", "P000"),
    GWANGJU("광주", "E000"),
    JEONNAM("전남", "L000"),
    JEONBUK("전북", "M000"),
    DAEGU("대구", "F000"),
    GYEONGBUK("경북", "D000"),
    BUSAN("부산", "H000"),
    ULSAN("울산", "J000"),
    GYEONGNAM("경남", "C000"),
    GANGWON("강원", "A000"),
    JEJU("제주", "N000");

    private final String district;
    private final String jobkoreaCode;

    Location(String district, String jobkoreaCode) {
        this.district = district;
        this.jobkoreaCode = jobkoreaCode;
    }

    public String koName() {
        return district;
    }

    public String jobkoreaCode() {
        return jobkoreaCode;
    }
}
