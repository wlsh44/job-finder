package flab.project.jobfinder.enums;

public enum JobType {
    FULL_TIME("1"),
    TEMPORARY("2"),
    INTERN("3"),
    FREELANCER("6"),
    MILITARY("9");

    private final String jobkoreaCode;

    JobType(String jobkoreaCode) {
        this.jobkoreaCode = jobkoreaCode;
    }

    public String jobkoreaCode() {
        return jobkoreaCode;
    }
}
