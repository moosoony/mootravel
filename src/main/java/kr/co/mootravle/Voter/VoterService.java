package kr.co.mootravle.Voter;

import kr.co.mootravle.Travel.Travel;
import kr.co.mootravle.User.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VoterService {

    private final VoterRepository voterRepository;

    public void create(Travel travel, SiteUser author){
        Voter voter = new Voter();
        voter.setTravel(travel);
        voter.setAuthor(author);
        this.voterRepository.save(voter);
    }
}
