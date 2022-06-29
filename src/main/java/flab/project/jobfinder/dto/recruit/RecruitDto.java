package flab.project.jobfinder.dto.recruit;

import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.enums.Platform;
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
    private boolean isAlwaysRecruiting;
    private String platform;

    public Recruit toEntity() {
        return Recruit.builder()
                .title(title)
                .url(url)
                .location(location)
                .techStack(techStack)
                .corp(corp)
                .dueDate(dueDate)
                .isAlwaysRecruiting(isAlwaysRecruiting)
                .platform(Platform.valueOf(platform))
                .jobType(jobType).build();
    }
}
