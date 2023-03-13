package kr.co.mootravle.User;

import kr.co.mootravle.Answer.AnswerRepository;
import kr.co.mootravle.Travel.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AnswerRepository answerRepository;
    private final TravelRepository travelRepository;

    //    회원가입 페이지
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "user/signup_form";
    }

    //    회원가입
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "user/signup_form";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "user/signup_form";
        }

        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getPassword1(), userCreateForm.getEmail(),
                    userCreateForm.getSex(), userCreateForm.getBirthday());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "user/signup_form";
        } catch (Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFaild", e.getMessage());
            return "user/signup_form";
        }

        return "redirect:/";
    }

    //    로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "/user/login_form";
    }

    //    사용자 정보 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/account")
    public String account(Model model, UserModifyForm userModifyForm, Principal principal) {
        SiteUser user = this.userService.getUser(principal.getName());
        if (!user.getUsername().equals(principal.getName())) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        String savedNm = user.getSavedNm();
        userModifyForm.setUsername(user.getUsername());
        userModifyForm.setEmail(user.getEmail());
        userModifyForm.setSex(user.getSex());
        userModifyForm.setBirthday(user.getBirthday());
        model.addAttribute("savedNm", savedNm);

        return "/user/user_account";
    }

    // 사용자 정보 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/account")
    public String accountModify(
            @Valid UserModifyForm userModifyForm, BindingResult bindingResult, Principal principal) throws IOException {
        if (bindingResult.hasErrors()) {
            return "user/user_account";
        }
        SiteUser user = this.userService.getUser(principal.getName());
        if (!user.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.userService.modify(user, userModifyForm.getFile(), userModifyForm.getEmail(), userModifyForm.getSex(), userModifyForm.getBirthday());
        return String.format("redirect:/");
    }


    // 사용자 정보 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete")
    public String deleteuser(Principal principal) {
        SiteUser siteuser = this.userService.getUser(principal.getName());
        if (!siteuser.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        Integer id = Math.toIntExact(siteuser.getId());

//        this.answerRepository.deleteById(id);
//        this.travelRepository.deleteById(id);
        this.userService.delete(siteuser);

        return "redirect:/user/logout";
    }

    // 사용자 비밀번호 페이지
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/password")
    public String password(Model model) {
        model.addAttribute("userPasswordForm", new UserPasswordForm());
        return "user/user_password";
    }

    // 사용자 비밀번호 수정
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/password")
    public String accountModify(@Valid UserPasswordForm userPasswordForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "user/user_password";
        }
        if (!userPasswordForm.getPassword1().equals(userPasswordForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "user/user_password";
        }
        SiteUser user = this.userService.getUser(principal.getName());
        if (!user.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.userService.password(user, userPasswordForm.getPassword1());
        return String.format("redirect:/user/logout");
    }

    // 사용자 활동 페이지
    @GetMapping("/activity")
    public String connections() {
        return "/user/user_activity";
    }

}
