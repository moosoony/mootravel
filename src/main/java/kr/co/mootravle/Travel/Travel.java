package kr.co.mootravle.Travel;

import kr.co.mootravle.Reply.Reply;
import kr.co.mootravle.User.SiteUser;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@DynamicInsert
@Data
@Entity
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private SiteUser author;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer viewcnt;

    private String travelStart;
    private String travelEnd;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private String expenses;

    @OneToMany(mappedBy = "travel", cascade = CascadeType.REMOVE)
    private List<Reply> replyList;

    @ManyToMany
    Set<SiteUser> voter;

}
