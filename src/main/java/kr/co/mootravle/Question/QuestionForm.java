package kr.co.mootravle.Question;

import kr.co.mootravle.File.FileEntity;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class QuestionForm {
    @NotEmpty(message="제목은 필수항목입니다.")
    @Size(max=200)
    private String subject;
    
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}
