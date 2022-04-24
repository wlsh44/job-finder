package flab.project.jobfinder.dto;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class ParseDto {
    private String title;
    private String corp;
    private String url;
    private String location;
    private String pay;
    private String techStack;
    private String jobType;
    private String career;
    private String date;
    private String platform;
}
