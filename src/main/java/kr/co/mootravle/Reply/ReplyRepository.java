package kr.co.mootravle.Reply;

import kr.co.mootravle.Travel.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    //    페이징 구현 메서드
    Page<Reply> findAll(Pageable pageable);

    // 사용자가 작성한 댓글
    @Query("Select r from Reply r where r.author.id=:id")
    Page<Reply> findByAuthorId(Pageable pageable, Long id);

    // 사용자가 작성한 댓글의 수
    @Query("Select count(r) from Reply r where r.author.id=:id")
    Long getCount(Long id);


    // 사용자가 작성한 댓글의 글 페이징
    @Query("Select distinct r.travel from Reply r where r.author.id=:id order by r.travel.createDate desc")
    Page<Travel> findByTravelId(Pageable pageable, Long id);

    // 사용자가 작성한 댓글의 글 수
    @Query("select count(r.travel.id) from Reply r where r.author.id=:id")
    Long getCountByAuthorId(Long id);
}

