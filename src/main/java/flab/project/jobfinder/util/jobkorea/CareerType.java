package flab.project.jobfinder.util.jobkorea;

public enum CareerType {
    JUNIOR("1"),
    SENIOR("2"),
    ANY("4");

    private final String jobkoreaCode;

    CareerType(String jobkoreaCode) {
        this.jobkoreaCode = jobkoreaCode;
    }

    public String jobkoreaCode() {
        return jobkoreaCode;
    }
}
