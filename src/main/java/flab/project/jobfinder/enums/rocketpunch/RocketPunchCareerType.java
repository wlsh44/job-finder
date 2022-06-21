package flab.project.jobfinder.enums.rocketpunch;

import flab.project.jobfinder.enums.PlatformCode;

public enum RocketPunchCareerType implements PlatformCode {
    JUNIOR("1"),
    SENIOR("2"),
    ANY("3");

    private final String code;

    RocketPunchCareerType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }
}
