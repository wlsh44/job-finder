package flab.project.jobfinder.enums.jobkorea;

import flab.project.jobfinder.enums.PlatformCode;

public enum JobKoreaJobType implements PlatformCode {
    FULL_TIME("1"),
    TEMPORARY("2"),
    INTERN("3"),
    FREELANCER("6"),
    MILITARY("9");

    private final String jobkoreaCode;

    JobKoreaJobType(String jobkoreaCode) {
        this.jobkoreaCode = jobkoreaCode;
    }

    @Override
    public String code() {
        return jobkoreaCode;
    }
}
