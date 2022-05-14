package flab.project.jobfinder.enums;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Platform {
    JOBKOREA("잡코리아"),
    ROCKETPUNCH("로켓펀치");

    private final String koreaName;
    private final static Map<String, String> map = Stream.of(values())
            .collect(Collectors.toUnmodifiableMap(Platform::koreaName, Platform::name));

    Platform(String koreaName) {
        this.koreaName = koreaName;
    }

    public String koreaName() {
        return koreaName;
    }

    public static Map<String, String> getKoreaNameMap() {
        return map;
    }
}
