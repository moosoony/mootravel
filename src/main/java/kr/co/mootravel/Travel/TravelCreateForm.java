package kr.co.mootravel.Travel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
public class TravelCreateForm {
    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max = 200)
    private String subject;

    @NotEmpty(message = "카테고리는 필수항목입니다.")
    private String category;

    @NotEmpty(message = "여행 시작일은 필수항목입니다.")
    private String travelStart;

    @NotEmpty(message = "여행 종료일은 필수항목입니다.")
    private String travelEnd;

    private MultipartFile file;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}
