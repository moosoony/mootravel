package kr.co.mootravel.File;

import kr.co.mootravel.Question.Question;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Table(name = "file")
@Entity
@Data
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;
    private String orgNm;
    private String savedNm;
    private String savedPath;

    @Builder
    public FileEntity(Long id, String orgNm, String savedNm, String savedPath) {
        this.id = id;
        this.orgNm = orgNm;
        this.savedNm = savedNm;
        this.savedPath = savedPath;
    }

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
