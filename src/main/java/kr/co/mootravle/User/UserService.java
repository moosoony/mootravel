package kr.co.mootravle.User;

import kr.co.mootravle.DataNotFoundException;
import kr.co.mootravle.Question.Question;
import kr.co.mootravle.Question.QuestionRepository;
import kr.co.mootravle.Reply.ReplyRepository;
import kr.co.mootravle.Travel.Travel;
import kr.co.mootravle.Travel.TravelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final QuestionRepository questionRepository;
    private final TravelRepository travelRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${file.dir}")
    private String fileDir;

    // 사용자가 작성한 댓글의 글
//    public Page<Travel> getList(int page, Long id){
//        List<Sort.Order> sorts = new ArrayList<>();
//        sorts.add(Sort.Order.desc("createDate"));
//        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
//        return this.replyRepository.findByTravelId(id);
//    }


//     사용자가 작성한 댓글의 글 (페이징 안하고 성공)
    public List<Travel> getTravelId(int page, Long id){

        List<Travel> travelId = replyRepository.findByTravelId(id);

        List<Travel> replyonpost = new ArrayList<>();

//        List<Sort.Order> sorts = new ArrayList<>();



        for (int i = 0; i < travelId.size(); i++) {
//            sorts.add(Sort.Order.desc("createDate"));
//            Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
            replyonpost.add(travelRepository.findAllById(travelId.get(i).getId()));
        }

        return replyonpost;
    }

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
    public void delete(SiteUser siteUser) {
        this.userRepository.delete(siteUser);
    }

    // 사용자 비밀번호 수정
    public void password(SiteUser user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(user);
    }

}
