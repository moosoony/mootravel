package kr.co.mootravel.Travel;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class TravelInsertForm {
    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max = 200)
    private String subject;

    private String category;

    @NotEmpty(message = "여행 시작일은 필수항목입니다.")
    private String travelStart;

    @NotEmpty(message = "여행 종료일은 필수항목입니다.")
    private String travelEnd;

    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;

    // 여행지 place_id
    private String place_id;
}
