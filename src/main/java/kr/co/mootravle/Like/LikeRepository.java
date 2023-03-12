package kr.co.mootravle.Like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    //    좋아요 조회
    @Query(value = "select l.id from Like l where l.author.id=:aid and l.travel.id=:tid")
    public String like(Long aid, Integer tid);
}
