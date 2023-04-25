package kr.co.mootravel.Answer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    void deleteByAuthorId(Long userid);

    void deleteByQuestionId(Integer id);
}
