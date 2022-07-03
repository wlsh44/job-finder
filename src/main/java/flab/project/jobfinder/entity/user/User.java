package flab.project.jobfinder.entity.user;

import flab.project.jobfinder.entity.recruit.Category;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Category> categories;
}
