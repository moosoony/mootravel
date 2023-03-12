package kr.co.mootravle.Like;

import kr.co.mootravle.Travel.Travel;
import kr.co.mootravle.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public void create(Travel travel, SiteUser author) {
        Like like = new Like();
        like.setTravel(travel);
        like.setAuthor(author);
        this.likeRepository.save(like);
    }

    // 좋아요 조회 서비스
    public boolean getLike(Long aid, Integer tid) {
        if (this.likeRepository.like(aid, tid)==null) {
            return true;
        } else {
            return false;
        }
    }

}
