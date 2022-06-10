package flab.project.jobfinder.enums;

public enum JobType {
    FULL_TIME("1", "0"),
    TEMPORARY("2", "1"),
    INTERN("3", "4"),
    FREELANCER("6", "2"),
    MILITARY("9", "3");

    private final String jobkoreaCode;
    private final String rocketPunchCode;

    JobType(String jobkoreaCode, String rocketPunchCode) {
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
