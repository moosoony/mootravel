package kr.co.mootravle.Travel;

import kr.co.mootravle.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
@Service
public class TravelService {
    private final TravelRepository travelRepository;

    public void create(String subject, String content, SiteUser user){
        Travel t = new Travel();
        t.setSubject(subject);
        t.setContent(content);
        t.setCreateDate(LocalDateTime.now());
        this.travelRepository.save(t);
    }
}
