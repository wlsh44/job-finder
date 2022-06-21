package flab.project.jobfinder.enums.jobkorea;

import flab.project.jobfinder.enums.PlatformCode;

public enum JobKoreaPayType implements PlatformCode {
    ANNUAL("1"),
    MONTH("2"),
    WEEK("3");

    private final String code;

    JobKoreaPayType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }
}
