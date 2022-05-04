package flab.project.jobfinder.enums;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Platform {
    JOBKOREA("잡코리아");

    private final String koreaName;

    Platform(String koreaName) {
        this.koreaName = koreaName;
    }

    public String koreaName() {
        return koreaName;
    }

    public static Map<String, String> toMap() {
        return Collections.unmodifiableMap((Stream
                .of(values())
                .collect(Collectors.toMap(Platform::koreaName, Platform::name))));
    }
}
