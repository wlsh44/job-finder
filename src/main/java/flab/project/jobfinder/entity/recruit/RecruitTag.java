package flab.project.jobfinder.entity.recruit;

import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "Recruit_Tag")
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class RecruitTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recruit_id")
    private Recruit recruit;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;
}
