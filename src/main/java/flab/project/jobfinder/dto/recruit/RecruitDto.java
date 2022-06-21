package flab.project.jobfinder.dto.recruit;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

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
    private LocalDate dueDate;
    private boolean alwaysRecruit;
    private String platform;
}
