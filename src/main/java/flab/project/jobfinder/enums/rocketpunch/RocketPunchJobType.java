package flab.project.jobfinder.enums.rocketpunch;

import flab.project.jobfinder.enums.PlatformCode;

public enum RocketPunchJobType implements PlatformCode {
    FULL_TIME("0"),
    TEMPORARY("1"),
    INTERN("4"),
    FREELANCER("2"),
    MILITARY("3");

    private final String code;

    RocketPunchJobType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }
}
