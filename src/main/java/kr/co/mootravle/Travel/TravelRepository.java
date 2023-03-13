package kr.co.mootravle.Travel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Integer> {
    @Modifying
    @Query(value = "update Travel t set t.viewcnt=t.viewcnt+1 where t.id=:id")
    void updateCount(Integer id);

    //    페이징 구현 메서드
    Page<Travel> findAll(Pageable pageable);

    //    검색 구현 메서드
    Page<Travel> findAll(Specification<Travel> spec, Pageable pageable);

    Page<Travel> findAllByUserId(Pageable pageable, Integer id);

    // Top3 구현 메서드
//    @Query("select "
//            + "t.travel "
//            + "from TravelVoter t "
//            + "group by t.travel "
//            + "order by count(t.id) desc "
//            + "limit 3")
//    List<Travel> top3();


}
