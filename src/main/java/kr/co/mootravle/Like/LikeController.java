package kr.co.mootravle.Like;

import kr.co.mootravle.Travel.Travel;
import kr.co.mootravle.Travel.TravelService;
import kr.co.mootravle.User.SiteUser;
import kr.co.mootravle.User.UserRepository;
import kr.co.mootravle.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;


@RequestMapping("/vote")
@RequiredArgsConstructor
@Controller
public class LikeController {
    private final TravelService travelService;
    private final LikeService likeService;
    private final UserService userService;
    private final LikeRepository likeRepository;

    private final UserRepository userRepository;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/{id}")
    public String createlike(@PathVariable("id") Integer id,
                             @Valid Principal principal) {
        // 게시글의 id
        Travel travel = this.travelService.getTravel(id);

        SiteUser siteUser = this.userService.getUser(principal.getName());
        System.out.println(siteUser);

        // 사용자의 id
        Long aid = (long) Math.toIntExact(siteUser.getId());

            this.likeService.create(travel, siteUser);
            return String.format("redirect:/travel/detail/%s", id);
//        }
    }

}
