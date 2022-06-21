package flab.project.jobfinder.enums.jobkorea;

import flab.project.jobfinder.enums.PlatformCode;

public enum JobKoreaLocation implements PlatformCode {
    SEOUL("I000"),
    GANGNAM("I010"),
    GANGDONG("I020"),
    GANGBUK("I030"),
    GANGSEO("I040"),
    GWANAK("I050"),
    GWANGJIN("I060"),
    GURO("I070"),
    GEUMCHEON("I080"),
    NOWON("I090"),
    DOBONG("I100"),
    DONGDAEMUN("I110"),
    DONGJAK("I120"),
    MAPO("I130"),
    SEODAEMUN("I140"),
    SEOCHO("I150"),
    SEODONG("I160"),
    SEONGBUK("I170"),
    SONGPA("I180"),
    YANGCHEON("I190"),
    YEONGDEUNGPO("I200"),
    YONGSAN("I210"),
    EUNPYEONG("I220"),
    JONGNO("I230"),
    JUNG("I240"),
    JUNGRANG("I250"),
    GYEONGGI("B000"),
    BUNDANG("B150"),
    INCHEON("K000"),
    DAEJEON("G000"),
    SEJONG("1000"),
    CHUNGNAM("O000"),
    CHUNGBUK("P000"),
    GWANGJU("E000"),
    JEONNAM("L000"),
    JEONBUK("M000"),
    DAEGU("F000"),
    GYEONGBUK("D000"),
    BUSAN("H000"),
    ULSAN("J000"),
    GYEONGNAM("C000"),
    GANGWON("A000"),
    JEJU("N000");

    private final String code;

    JobKoreaLocation(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }
}