package kr.co.mootravel.Travel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Integer> {


    //    페이징 구현 메서드
    Page<Travel> findAll(Pageable pageable);

    //    검색 구현 메서드
    Page<Travel> findAll(Specification<Travel> spec, Pageable pageable);

    // 조회수 증가
    @Modifying
    @Query(value = "update Travel t set t.viewcnt=t.viewcnt+1 where t.id=:id")
    void updateCount(Integer id);

    // 사용자가 작성한 게시글 페이징
    @Query("select t from Travel t where t.author.id =:id")
    Page<Travel> findByTravel(Pageable pageable, Long id);


    // 사용자가 작성한 댓글의 글 (페이징 안하고 성공)
//    Travel findAllById(Integer id);

    // 사용자가 작성한 게시글 수 조회
    @Query("select count(t) from Travel t where t.author.id =:id")
    Long getCount(Long id);

    // 사용자가 작성한 게시글 삭제
    @Modifying
    @Transactional
    @Query("delete from Travel t where t.author.id =:id")
    void deleteByAuthorId(Long id);
    
}
