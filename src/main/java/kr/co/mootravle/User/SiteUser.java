package kr.co.mootravle.User;

import kr.co.mootravle.Voter.Voter;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Table(name = "user")
@Data
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) // 유일한 값만 저장할 수 있음
    private String username;

    private String password;

    @Column(unique = true) // 유일한 값만 저장할 수 있음
    private String email;

    private String sex;

    private String birthday;
}
