package kr.co.mootravel.Reply;

import kr.co.mootravel.Travel.Travel;
import kr.co.mootravel.User.SiteUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private String TravelSubject;

    @ManyToOne
    private Travel travel;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;
}
