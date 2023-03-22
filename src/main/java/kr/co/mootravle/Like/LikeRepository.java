package kr.co.mootravle.Like;

import kr.co.mootravle.Travel.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    //    좋아요 여부 조회
    @Query(value = "select l.id from Like l where l.author.id=:aid and l.travel.id=:tid")
    public String like(Long aid, Integer tid);

    //    사용자가 좋아요한 게시글 페이징
    @Query(value = "select l.travel.id from Like l where l.author.id=:id")
    Page<Travel> LikeByTravelId(Pageable pageable, Long id);

    //    사용자가 좋아요한 게시글 수
    @Query(value = "select count(l) from Like l where l.author.id=:id")
    Long getCount(Long id);

    // Top3 구현 메서드
    @Query("SELECT l FROM Like l " +
            "WHERE YEAR(l.travel.createDate) = YEAR(CURRENT_DATE) " +
            "AND MONTH(l.travel.createDate) = MONTH(CURRENT_DATE) " +
            "group by l.id " +
            "order by count(l.author.id) desc")
    List<Like> findTop3TravelByThisMonth(Pageable pageable);
}
