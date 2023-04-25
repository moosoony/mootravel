package kr.co.mootravel.Like;

import kr.co.mootravel.Travel.Travel;
import kr.co.mootravel.Travel.TravelService;
import kr.co.mootravel.User.SiteUser;
import kr.co.mootravel.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;


@RequestMapping("/vote")
@RequiredArgsConstructor
@Controller
public class LikeController {
    private final TravelService travelService;
    private final LikeService likeService;
    private final UserService userService;
    private final LikeRepository likeRepository;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create/{id}")
    public String createlike(Model model, @PathVariable("id") Integer id,
                             @Valid Principal principal) {
        // 사용자의 아이디
        SiteUser siteUser = this.userService.getUser(principal.getName());
        Long aid = (long) Math.toIntExact(siteUser.getId());

        // Travel 게시글의 아이디
        Travel travel = this.travelService.getTravel(id);
        Integer tid = travel.getId();

        // 좋아요 내역이 없으면
        if (likeService.getLike(aid, tid) == true) {

            // 좋아요
            this.likeService.create(travel, siteUser);
        }

        // 좋아요 내역이 있으면
        else {
            // 좋아요 취소
            Integer likeId = Integer.valueOf(this.likeRepository.like(aid, tid));
//            likeRepository.deleteById(likeId);
            this.likeService.delete(Long.valueOf(likeId));
        }


        return String.format("redirect:/travel/detail/%s", id);
    }
}
