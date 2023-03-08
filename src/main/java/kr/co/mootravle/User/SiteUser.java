package kr.co.mootravle.User;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Table(name = "user")
@NoArgsConstructor
@DynamicInsert
@Data
@Entity
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    사용자 ID
    @Column(unique = true) // 유일한 값만 저장할 수 있음
    private String username;

    //    사용자 비밀번호
    private String password;

    //    사용자 E-Mail
    @Column(unique = true) // 유일한 값만 저장할 수 있음
    private String email;

    //    사용자 성별
    private String sex;

    //    사용자 생일
    private String birthday;

    //    사용자 프로필 사진
    @Column(nullable = true)
    private String orgNm;
    @Column(nullable = true)
    private String savedNm;
    @Column(nullable = true)
    private String savedPath;

    @Builder
    public SiteUser(String orgNm, String savedNm, String savedPath) {
        this.orgNm = orgNm;
        this.savedNm = savedNm;
        this.savedPath = savedPath;
    }
}
