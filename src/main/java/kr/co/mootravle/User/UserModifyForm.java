package kr.co.mootravle.User;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserModifyForm {
    private MultipartFile file;

    @Size(min = 3, max = 10, message = "사용자 ID는 3에서 10 사이여야 합니다.")
    @NotEmpty(message = "사용자 ID는 필수항목입니다.")
    private String username;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email(message = "이메일 형식으로 입력바랍니다.")
    private String email;

    private String sex;

    private String birthday;
}