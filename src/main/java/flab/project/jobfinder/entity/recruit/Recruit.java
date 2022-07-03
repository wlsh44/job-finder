package flab.project.jobfinder.entity.recruit;

import flab.project.jobfinder.converter.PlatformConverter;
import flab.project.jobfinder.enums.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Recruit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String url;

    @Column(length = 20, nullable = false)
    @Convert(converter = PlatformConverter.class)
    private Platform platform;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "Recruit_Tag",
            joinColumns = @JoinColumn(name = "recruit_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private List<Tag> tags;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private boolean isAlwaysRecruiting;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDate dueDate;

    @Column(length = 20, nullable = false)
    private String corp;

    @Column(length = 10)
    private String jobType;

    @Column(length = 10)
    private String career;

    private String location;
    private String techStack;
}
