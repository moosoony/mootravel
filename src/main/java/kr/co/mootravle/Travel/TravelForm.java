package kr.co.mootravle.Travel;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;


@Data
public class TravelForm {
    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max=200)
    private String subject;

    @NotNull(message = "썸네일은 필수항목입니다.")
    private MultipartFile file;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

    @NotEmpty(message = "일정은 필수항목입니다.")
    private String travelStart;

    @NotEmpty(message = "일정은 필수항목입니다.")
    private String travelEnd;

    @NotEmpty(message = "경비는 필수항목입니다.")
    @PositiveOrZero(message = "경비는 숫자 0 이상이어야 합니다.")
    private String expenses;
}
