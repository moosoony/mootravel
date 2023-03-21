package kr.co.mootravle.Reply;

import kr.co.mootravle.Question.Question;
import kr.co.mootravle.Travel.Travel;
import kr.co.mootravle.Travel.TravelRepository;
import kr.co.mootravle.User.SiteUser;
import kr.co.mootravle.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReplyService {

    public final ReplyRepository replyRepository;

    private final UserService userService;
    private final TravelRepository travelRepository;


    //    Account/Activity/Reply 페이징 구현 서비스
    public Page<Reply> getList(int page, Long id) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.replyRepository.findByAuthorId(pageable, id);
    }


    // 사용자가 작성한 댓글의 글
//    public List<Integer> getTravelId(Long id) {
//        List<Integer> travelId = userService.getTravelId(id);
//
//        List<Travel> replyonpost = new ArrayList<>();
//        for (int i = 0; i < travelId.size(); i++) {
//            replyonpost.add(travelRepository.findAllById(travelId.get(i)));
//
//        }
//        return this.replyRepository.findByTravelId(id);
//    }
//    public List<Integer> getTravelId(Long id){
//        return this.replyRepository.findByTravelId(id);
//    }

    public void create(Travel travel, String content, SiteUser author) {
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setCreateDate(LocalDateTime.now());
        reply.setTravel(travel);
        reply.setTravelSubject(travel.getSubject());
        reply.setAuthor(author);
        this.replyRepository.save(reply);
    }

    //    삭제하기 서비스
    public void delete(Integer id) {
        this.replyRepository.deleteById(id);
    }

    // 사용자가 작성한 댓글 수
    public Long getCount(Long id) {
        return this.replyRepository.getCount(id);
    }
}
