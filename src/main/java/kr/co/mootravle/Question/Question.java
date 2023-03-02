package kr.co.mootravle.Question;

import kr.co.mootravle.Answer.Answer;
import kr.co.mootravle.File.FileEntity;
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
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;

    @ManyToMany
    Set<SiteUser> voter;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer viewcnt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<FileEntity> fileList;
}
