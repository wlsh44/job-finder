package flab.project.jobfinder.util;

public enum CareerType {
    JUNIOR("1"),
    SENIOR("2"),
    ANY("4");

    private String jobkoreaCode;

    CareerType(String jobkoreaCode) {
        this.jobkoreaCode = jobkoreaCode;
    }
}
