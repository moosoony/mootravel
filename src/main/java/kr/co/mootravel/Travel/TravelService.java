package kr.co.mootravel.Travel;

import kr.co.mootravel.DataNotFoundException;
import kr.co.mootravel.Reply.ReplyRepository;
import kr.co.mootravel.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Transactional
@RequiredArgsConstructor
@Service
public class TravelService {
    private final TravelRepository travelRepository;

    private final ReplyRepository replyRepository;
    @Value("${file.dir}")
    private String fileDir;

    //    Account/Activity/Post 페이징 구현 서비스
    public Page<Travel> getList(int page, Long id) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.travelRepository.findByTravel(pageable, id);
    }

    //    Account/Activity/Reply on Post 페이징 구현 서비스
//    public Page<Travel> getReplyonPostList(int page, Long id){
//        List<Travel> travelId = userService.getTravelId(id);
//        Page<Travel> replyonpost = null;
//        for (int i = 0; i < travelId.size(); i++) {
//            replyonpost= (Page<Travel>) travelRepository.findAllById(travelId.get(i).getId());
//        }
//
//        List<Sort.Order> sorts = new ArrayList<>();
//        sorts.add(Sort.Order.desc("createDate"));
//        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
//        return this.travelRepository.findByAuthorId(pageable,id);
//    }


    //    페이징, 검색 키워드 구현 서비스
    public Page<Travel> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 9, Sort.by(sorts));
        Specification<Travel> spec = search(kw);
        return this.travelRepository.findAll(spec, pageable);
    }

    //    검색 구현 서비스
    public Specification<Travel> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Travel> t, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true); // 중복을 제거
                Join<Travel, SiteUser> u1 = t.join("author", JoinType.LEFT);
                return cb.or(cb.like(t.get("subject"), "%" + kw + "%"),   // 제목
                        cb.like(t.get("content"), "%" + kw + "%"),  // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"));    // 게시글 작성자
            }
        };
    }

    //   작성하기 서비스
    public void create(String subject, MultipartFile file, String content, SiteUser user, String travelStart, String travelEnd) throws IOException {

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

        Travel t = new Travel();
        t.setAuthor(user);
        t.setSubject(subject);
        t.setOrgNm(origName);
        t.setSavedNm(savedName);
        t.setSavedPath(savedPath);
        t.setContent(content);
        t.setCreateDate(LocalDateTime.now());
        t.setTravelStart(travelStart);
        t.setTravelEnd(travelEnd);
        this.travelRepository.save(t);
    }

    //   작성하기 서비스2
    public void create(String subject, String content, SiteUser user, String travelStart, String travelEnd,
                       String place_id) throws IOException {
        Travel t = new Travel();
        t.setAuthor(user);
        t.setSubject(subject);
        t.setContent(content);
        t.setCreateDate(LocalDateTime.now());
        t.setTravelStart(travelStart);
        t.setTravelEnd(travelEnd);
        t.setPlace_id(place_id);

        this.travelRepository.save(t);
    }


    //    상세보기 서비스
    public Travel getTravel(Integer id) {
        Optional<Travel> travel = this.travelRepository.findById(id);
        if (travel.isPresent()) {
            return travel.get();
        } else {
            throw new DataNotFoundException("travel not found");
        }
    }

    //    조회수 구현 서비스
    public void updateviewcnt(Integer id) {
        travelRepository.updateCount(id);
    }

    //    수정 서비스
    public void modify(Travel travel, @NotEmpty(message = "제목은 필수항목입니다.") @Size(max = 200) String travelFormSubject, @NotEmpty(message = "내용은 필수항목입니다.") String travelFormContent, @NotEmpty(message = "일정은 필수항목입니다.") String travelStart, String travelEnd, MultipartFile file) throws IOException {
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

        travel.setSubject(travelFormSubject);
        travel.setContent(travelFormContent);
        travel.setTravelStart(travelStart);
        travel.setTravelEnd(travelEnd);
        travel.setOrgNm(origName);
        travel.setSavedNm(savedName);
        travel.setSavedPath(savedPath);
        travel.setModifyDate(LocalDateTime.now());
        this.travelRepository.save(travel);
    }

    //    삭제 서비스
    public void delete(Travel travel) {
        this.travelRepository.delete(travel);
    }

    //    사용자 아이디로 삭제 서비스
    public void deleteByAuthorId(Long id) {
        this.travelRepository.deleteByAuthorId(id);
    }


    //    사용자가 작성한 게시글 수
    public Long getCount(Long id) {
        return this.travelRepository.getCount(id);
    }
    //    수정 서비스 수정 중
    public void modify(Travel travel, String subject, String content, String travelStart, String travelEnd, String placeId) {
        travel.setSubject(subject);
        travel.setContent(content);
        travel.setTravelStart(travelStart);
        travel.setTravelEnd(travelEnd);
        travel.setModifyDate(LocalDateTime.now());
        travel.setPlace_id(placeId);
        this.travelRepository.save(travel);
    }
}
