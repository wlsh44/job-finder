package flab.project.jobfinder.enums;

public enum PayType {
    ANNUAL("1"),
    MONTH("2"),
    WEEK("3");

    private final String jobkoreaCode;

    PayType (String jobkoreaCode) {
        this.jobkoreaCode = jobkoreaCode;
    }

    public String jobkoreaCode() {
        return jobkoreaCode;
    }
}
