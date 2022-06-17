package flab.project.jobfinder.enums.rocketpunch;

import flab.project.jobfinder.enums.PlatformCode;

public enum RocketPunchCareerType implements PlatformCode {
    JUNIOR("1"),
    SENIOR("2"),
    ANY("3");

    private final String rocketPunchCode;

    RocketPunchCareerType(String rocketPunchCode) {
        this.rocketPunchCode = rocketPunchCode;
    }

    @Override
    public String code() {
        return rocketPunchCode;
    }
}
