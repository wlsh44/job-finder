package flab.project.jobfinder.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class RecruitDto {
    private String title;
    private String corp;
    private String url;
    private String location;
    private String pay;
    private String techStack;
    private String jobType;
    private String career;
    private String dueDate;
    private String platform;
}
