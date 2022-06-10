package flab.project.jobfinder.enums;

public enum CareerType {
    JUNIOR("1", "1"),
    SENIOR("2", "2"),
    ANY("4", "3");

    private final String jobkoreaCode;
    private final String rocketPunchCode;

    CareerType(String jobkoreaCode, String rocketPunchCode) {
        this.jobkoreaCode = jobkoreaCode;
        this.rocketPunchCode = rocketPunchCode;
    }

    public String jobkoreaCode() {
        return jobkoreaCode;
    }

    public String rocketPunchCode() {
        return rocketPunchCode;
    }
}
