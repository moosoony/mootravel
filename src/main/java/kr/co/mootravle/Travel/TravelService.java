package kr.co.mootravle.Travel;

import kr.co.mootravle.DataNotFoundException;
import kr.co.mootravle.User.SiteUser;
import kr.co.mootravle.Voter.Voter;
import kr.co.mootravle.Voter.VoterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Transactional
@RequiredArgsConstructor
@Service
public class TravelService {
    private final TravelRepository travelRepository;

    private final VoterRepository voterRepository;

//    페이징 구현 서비스
    public Page<Travel> getList(int page, String kw){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 9, Sort.by(sorts));
        Specification<Travel> spec = search(kw);
        return this.travelRepository.findAll(spec, pageable);
    }

    public Specification<Travel> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Travel> t, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true); // 중복을 제거
                Join<Travel, SiteUser> u1 = t.join("author", JoinType.LEFT);
                return cb.or(cb.like(t.get("subject"), "%" + kw + "%"),   // 제목
                        cb.like(t.get("content"), "%" + kw + "%"),  // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"));    // 게시글 작성자
            }
        };
    }

    public void create(String subject, String content, SiteUser user, String travelStart, String travelEnd, String expenses) {
        Travel t = new Travel();
        t.setAuthor(user);
        t.setSubject(subject);
        t.setContent(content);
        t.setCreateDate(LocalDateTime.now());
        t.setTravelStart(travelStart);
        t.setTravelEnd(travelEnd);
        t.setExpenses(expenses);
        this.travelRepository.save(t);
    }

    public Travel getTravel(Integer id){
        Optional<Travel> travel = this.travelRepository.findById(id);
        travelRepository.updateCount(id);
        if(travel.isPresent()){
            return travel.get();
        }else{
            throw new DataNotFoundException("travel not found");
        }
    }

    public void modify(Travel travel, String subject, String content){
        travel.setSubject(subject);
        travel.setContent(content);
        travel.setModifyDate(LocalDateTime.now());
        this.travelRepository.save(travel);
    }

    public void delete(Travel travel){
        this.travelRepository.delete(travel);
    }
}
