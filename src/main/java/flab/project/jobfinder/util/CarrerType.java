package flab.project.jobfinder.util;

public enum CarrerType {
    JUNIOR("1"),
    SENIOR("2"),
    ANY("4");

    private String jobkoreaCode;

    CarrerType(String jobkoreaCode) {
        this.jobkoreaCode = jobkoreaCode;
    }
}
