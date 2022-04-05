package flab.project.jobfinder.util;

public enum PayType {
    ANNUAL("1"),
    MONTH("2"),
    WEEK("3");

    private String jobkoreaCode;

    PayType (String jobkoreaCode) {
        this.jobkoreaCode = jobkoreaCode;
    }
}
