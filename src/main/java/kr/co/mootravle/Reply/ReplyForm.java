package kr.co.mootravle.Reply;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ReplyForm {
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}
