package kr.co.mootravel.Answer;

import kr.co.mootravel.Question.Question;
import kr.co.mootravel.User.SiteUser;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "question_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "FK_answer_question_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;

    @ManyToOne
    private SiteUser author;

    private LocalDateTime modifyDate;
}
