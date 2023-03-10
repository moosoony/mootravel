package kr.co.mootravle.Travel;

import kr.co.mootravle.Reply.Reply;
import kr.co.mootravle.User.SiteUser;
import kr.co.mootravle.Like.Like;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@NoArgsConstructor
@DynamicInsert
@Data
@Entity
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //    작성자
    @ManyToOne
    private SiteUser author;

    //    제목
    @Column(length = 200)
    private String subject;

    //    썸네일
    private String orgNm;
    private String savedNm;
    private String savedPath;

    @Builder
    public Travel(String orgNm, String savedNm, String savedPath) {
        this.orgNm = orgNm;
        this.savedNm = savedNm;
        this.savedPath = savedPath;
    }

    //    내용
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    //    작성일
    private LocalDateTime createDate;

    //    수정일
    private LocalDateTime modifyDate;

    //    조회수
    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer viewcnt;

    //    여행 시작일
    private String travelStart;

    //    여행 종료일
    private String travelEnd;

    //    여행 경비
    @Column(columnDefinition = "integer default 0", nullable = false)
    private String expenses;

    //    댓글
    @OneToMany(mappedBy = "travel", cascade = CascadeType.REMOVE)
    private List<Reply> replyList;

    //    좋아요
    @OneToMany(mappedBy = "travel", cascade = CascadeType.REMOVE)
    private Set<Like> voter;

}
