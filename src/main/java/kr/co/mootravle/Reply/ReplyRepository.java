package kr.co.mootravle.Reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {
    @Query("Select r from Reply r where r.author.id=:id")
    public List<Reply> findByAuthorId(Long id);

    @Query("Select distinct r.travel.id from Reply r where r.author.id=:id")
    public List<Integer> findByTravelId(Long id);
}
