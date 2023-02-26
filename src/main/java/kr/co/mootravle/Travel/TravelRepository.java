package kr.co.mootravle.Travel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Integer> {
    @Modifying
    @Query(value = "update Travel t set t.viewcnt=t.viewcnt+1 where t.id=:id")
    void updateCount(Integer id);
}
