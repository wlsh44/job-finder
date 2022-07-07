package flab.project.jobfinder.dto.recruit;

import flab.project.jobfinder.entity.recruit.Category;
import flab.project.jobfinder.entity.recruit.Recruit;
import flab.project.jobfinder.enums.Platform;
import lombok.*;

import java.time.LocalDate;

@Builder
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    public RecruitDto(Recruit recruit) {
        this.title = recruit.getTitle();
        this.corp = recruit.getCorp();
        this.url = recruit.getUrl();
        this.location = recruit.getLocation();
        this.techStack = recruit.getTechStack();
        this.jobType = recruit.getJobType();
        this.career = recruit.getCareer();
        this.dueDate = recruit.getDueDate();
        this.isAlwaysRecruiting = recruit.isAlwaysRecruiting();
        this.platform = recruit.getPlatform().koreaName();
    }

    public Recruit toEntity(Category category) {
        return Recruit.builder()
                .title(title)
                .url(url)
                .location(location)
                .techStack(techStack)
                .corp(corp)
                .dueDate(dueDate)
                .isAlwaysRecruiting(isAlwaysRecruiting)
                .platform(Platform.of(platform))
                .jobType(jobType)
                .category(category)
                .build();
    }
}
