package flab.project.jobfinder.enums.jobkorea;

import flab.project.jobfinder.enums.PlatformCode;

public enum JobKoreaCareerType implements PlatformCode {
    JUNIOR("1"),
    SENIOR("2"),
    ANY("4");

    private final String jobkoreaCode;

    JobKoreaCareerType(String jobkoreaCode) {
        this.jobkoreaCode = jobkoreaCode;
    }

    @Override
    public String code() {
        return jobkoreaCode;
    }
}
