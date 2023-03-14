package kr.co.mootravle.Reply;

import kr.co.mootravle.Travel.Travel;
import kr.co.mootravle.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReplyService {

    public final ReplyRepository replyRepository;


    public void create(Travel travel, String content, SiteUser author){
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setCreateDate(LocalDateTime.now());
        reply.setTravel(travel);
        reply.setTravelSubject(travel.getSubject());
        reply.setAuthor(author);
        this.replyRepository.save(reply);
    }
}
