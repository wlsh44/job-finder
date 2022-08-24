package flab.project.jobfinder.enums.jobkorea;

import flab.project.jobfinder.enums.PlatformCode;

public enum JobKoreaCareerType implements PlatformCode {
    JUNIOR("1"),
    SENIOR("2"),
    ANY("4");

    private final String code;

    JobKoreaCareerType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }
}
