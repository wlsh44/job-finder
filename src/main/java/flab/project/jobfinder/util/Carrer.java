package flab.project.jobfinder.util;

public enum Carrer {
    JUNIOR("1"),
    SENIOR("2"),
    ANY("4");

    private String jobkoreaCode;

    Carrer(String jobkoreaCode) {
        this.jobkoreaCode = jobkoreaCode;
    }
}
