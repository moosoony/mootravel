package kr.co.mootravel.Question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {

    //    페이징 구현 메서드
    Page<Question> findAll(Pageable pageable);

    //    검색 구현 메서드
    Page<Question> findAll(Specification<Question> spec, Pageable pageable);

    // 조회수 증가
    @Modifying
    @Query(value = "update Question q set q.viewcnt=q.viewcnt+1 where q.id=:id")
    void updateCount(Integer id);

    @Query("select "
            + "distinct q "
            + "from Question q "
            + "left outer join SiteUser u1 on q.author=u1 "
            + "left outer join Answer a on a.question=q "
            + "left outer join SiteUser u2 on a.author=u2 "
            + "where "
            + "   q.subject like %:kw% "
            + "   or q.content like %:kw% "
            + "   or u1.username like %:kw% "
            + "   or a.content like %:kw% "
            + "   or u2.username like %:kw% ")
    Page<Question> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    // 사용자가 작성한 문의글
    @Query("select q from Question q where q.author.id=:id")
    Page<Question> findByAuthorId(Pageable pageable,Long id);

    // 사용자가 작성한 문의글의 수
    @Query("select count(q) from Question q where q.author.id=:id")
    Long getCount(Long id);

    // 사용자가 작성한 문의글 삭제
    @Modifying
    @Transactional
    @Query("delete from Question q where q.author.id=:id")
    void deleteByAuthorId(Long id);

    // 사용자가 작성한 문의글의 Id
    @Query("select q.id from Question q where q.author.id=:id")
    List<Integer> findByAuthorId(Long id);
}
