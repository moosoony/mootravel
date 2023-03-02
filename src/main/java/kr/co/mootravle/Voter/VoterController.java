package kr.co.mootravle.Voter;

import kr.co.mootravle.Travel.Travel;
import kr.co.mootravle.Travel.TravelService;
import kr.co.mootravle.User.SiteUser;
import kr.co.mootravle.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;


@RequestMapping("/vote")
@RequiredArgsConstructor
@Controller
public class VoterController {
    private final TravelService travelService;
    private final VoterService voterService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/{id}")
    public String createvote(@PathVariable("id") Integer id,
                             @Valid Principal principal) {
        Travel travel = this.travelService.getTravel(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        System.out.println(siteUser);
        this.voterService.create(travel, siteUser);
        return String.format("redirect:/travel/detail/%s", id);
    }
}
