package flab.project.jobfinder.entity.recruit;

import flab.project.jobfinder.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"user_id", "name"})
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User userId;

    @ManyToMany(mappedBy = "categories")
    private List<Recruit> recruits;
}
