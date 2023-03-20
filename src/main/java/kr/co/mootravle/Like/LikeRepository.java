package kr.co.mootravle.Like;

import kr.co.mootravle.Travel.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    //    좋아요 여부 조회
    @Query(value = "select l.id from Like l where l.author.id=:aid and l.travel.id=:tid")
    public String like(Long aid, Integer tid);

    //    사용자가 좋아요한 게시글 페이징
    @Query(value = "select l.travel.id from Like l where l.author.id=:id")
    Page<Travel> LikeByTravelId(Pageable pageable, Long id);
}
