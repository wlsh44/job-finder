package flab.project.jobfinder.dto.member;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;
}
