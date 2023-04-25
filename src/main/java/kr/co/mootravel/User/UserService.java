package kr.co.mootravel.User;

import kr.co.mootravel.Answer.AnswerService;
import kr.co.mootravel.DataNotFoundException;
import kr.co.mootravel.Like.LikeService;
import kr.co.mootravel.Question.Question;
import kr.co.mootravel.Question.QuestionRepository;
import kr.co.mootravel.Question.QuestionService;
import kr.co.mootravel.Reply.ReplyRepository;
import kr.co.mootravel.Travel.TravelRepository;
import kr.co.mootravel.Travel.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final QuestionRepository questionRepository;

    private final PasswordEncoder passwordEncoder;

    private final LikeService likeService;

    private final TravelService travelService;

    private final QuestionService questionService;

    private final AnswerService answerService;

    
    @Value("${file.dir}")
    private String fileDir;

    // 사용자가 문의한 글
    public Page<Question> getQuestionList(Pageable pageable, Long id){
        return this.questionRepository.findByAuthorId(pageable, id);
    }

    public SiteUser create(String username, String password, String email, String sex, String birthday) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setSex(sex);
        user.setBirthday(birthday);
        this.userRepository.save(user);
        return user;
    }

    public void create(String username, String email, String password1) {
    }

    public SiteUser getUser(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        if (siteUser.isPresent()) {
            return siteUser.get();
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }

    //  사용자 계정 수정
    public void modify(SiteUser user, MultipartFile file, String email, String sex, String birthday) throws IOException {
        // 원래 파일 이름 추출
        String origName = file.getOriginalFilename();

        // 파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();

        // 확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));

        // uuid와 확장자 결합
        String savedName = uuid + extension;

        // 파일을 불러올 때 사용할 파일 경로
        String savedPath = fileDir + savedName;

        // 실제로 로컬에 uuid를 파일명으로 저장
        file.transferTo(new File(savedPath));

        user.setOrgNm(origName);
        user.setSavedNm(savedName);
        user.setSavedPath(savedPath);
        user.setEmail(email);
        user.setSex(sex);
        user.setBirthday(birthday);
        this.userRepository.save(user);
    }

    // 사용자 계정 삭제
    @Transactional
    public void delete(SiteUser siteUser) {

        // 사용자가 좋아요한 좋아요 취소
        this.likeService.deleteByAuthorId(siteUser.getId());
        // 사용자가 작성한 게시글 삭제
        this.travelService.deleteByAuthorId(siteUser.getId());

        // 사용자가 작성한 답변 삭제
        this.answerService.deleteByAuthorId(siteUser.getId());

        // 사용자가 작성한 문의글 삭제
        this.questionService.deleteByAuthorId(siteUser.getId());

        // 사용자 삭제
        this.userRepository.delete(siteUser);
    }

    // 사용자 비밀번호 수정
    public void password(SiteUser user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
    }

}
