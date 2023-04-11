package kr.co.mootravel.Question;

import kr.co.mootravel.Answer.Answer;
import kr.co.mootravel.DataNotFoundException;
import kr.co.mootravel.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    //  페이징, 검색 키워드 구현 서비스
    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAllByKeyword(kw, pageable);
    }

    //    작성하기 서비스
    public void create(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.questionRepository.save(q);
    }

    // 수정하기 서비스
    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    //    삭제하기 서비스
    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    //    상세보기 서비스
    public Question getQuestion(Integer id) {
        Optional<Question> question = this.questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    // 조회수 구현 서비스
    public void updateviewcnt(Integer id) {
        questionRepository.updateCount(id);
    }

    // 좋아요 생성 서비스
    public void like(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        this.questionRepository.save(question);
    }

    // 검색 키워드 서비스
    public Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true); // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"),   //제목
                        cb.like(q.get("content"), "%" + kw + "%"),  // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),  // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    //    Account/Activity/Reply 페이징 구현 서비스
    public Page<Question> getList(int page, Long id){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
        return this.questionRepository.findByAuthorId(pageable, id);
    }

    // 사용자가 작성한 문의글의 수
    public Long getCount(Long id){
        return this.questionRepository.getCount(id);
    }

    // 사용자가 작성한 문의글 삭제
    public void deleteByAuthorId(Long id){
        this.questionRepository.deleteByAuthorId(id);
    }
}
