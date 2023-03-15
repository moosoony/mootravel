package kr.co.mootravle.Like;

import kr.co.mootravle.Reply.Reply;
import kr.co.mootravle.Travel.Travel;
import kr.co.mootravle.Travel.TravelRepository;
import kr.co.mootravle.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;

    private final TravelRepository travelRepository;

    //    좋아요한 게시글 조회
    public List<Travel> getLikeByTravel(Long id) {
        return travelRepository.findByTravel(id);
    }

    //    Account/Activity/Like 페이징 구현 서비스
    public Page<Integer> getList(int page, Long id) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.likeRepository.LikeByTravelId(pageable, id);
    }

    public void create(Travel travel, SiteUser author) {
        Like like = new Like();
        like.setTravel(travel);
        like.setAuthor(author);
        like.setCreateDate(LocalDateTime.now());
        this.likeRepository.save(like);
    }

    // 좋아요 여부 조회 서비스
    public boolean getLike(Long aid, Integer tid) {
        if (this.likeRepository.like(aid, tid) == null) {
            return true;
        } else {
            return false;
        }
    }

    //    사용자가 좋아요 한 글
//    public Page<Travel> getLikeByTravelId(int page){
//        List<Sort.Order> sorts = new ArrayList<>();
//        sorts.add(Sort.Order.desc("createDate"));
//        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
//        return this.likeRepository.findByTravelId(id);
//    }

}
