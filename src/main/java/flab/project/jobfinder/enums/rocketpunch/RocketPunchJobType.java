package flab.project.jobfinder.enums.rocketpunch;

import flab.project.jobfinder.enums.PlatformCode;

public enum RocketPunchJobType implements PlatformCode {
    FULL_TIME("0"),
    TEMPORARY("1"),
    INTERN("4"),
    FREELANCER("2"),
    MILITARY("3");

    private final String rocketPunchCode;

    RocketPunchJobType(String rocketPunchCode) {
        this.rocketPunchCode = rocketPunchCode;
    }

    @Override
    public String code() {
        return rocketPunchCode;
    }
}
