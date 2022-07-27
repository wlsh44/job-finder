package flab.project.jobfinder.dto.bookmark;

import flab.project.jobfinder.entity.recruit.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TagResponseDto {
    private Long id;
    private String name;

    public TagResponseDto(Tag tag) {
        if (tag == null) {
            throw new NullPointerException();
        }
        this.id = tag.getId();
        this.name = tag.getName();
    }
}
