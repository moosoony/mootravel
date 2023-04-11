package kr.co.mootravel.Like;

import kr.co.mootravel.Travel.Travel;
import kr.co.mootravel.Travel.TravelRepository;
import kr.co.mootravel.User.SiteUser;
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

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;

    private final TravelRepository travelRepository;

    // 좋아요한 게시글 페이징 구현 서비스
    public Page<Travel> getLikeByTravel(int page, Long id) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return likeRepository.LikeByTravelId(pageable, id);
    }

    // 좋아요 생성 서비스
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

    // 사용자가 좋아요한 게시글 수
    public Long getCount(Long id){
        return this.likeRepository.getCount(id);
    }

    // Top3 구현 메서드
    public List<Travel> findTravelByThisMonth(){
        Pageable pageable = PageRequest.of(0, 3);
        return this.likeRepository.findTop3TravelByThisMonth(pageable);
    }

}
