package kr.co.mootravle.Reply;

import kr.co.mootravle.Travel.Travel;
import kr.co.mootravle.Travel.TravelService;
import kr.co.mootravle.User.SiteUser;
import kr.co.mootravle.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/reply")
@RequiredArgsConstructor
@Controller
public class ReplyController {
    private final TravelService travelService;
    private final ReplyService replyService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String createReply(Model model, @PathVariable("id") Integer id,
                              @Valid ReplyForm replyForm, BindingResult bindingResult, Principal principal){
        Travel travel = this.travelService.getTravel(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        if(bindingResult.hasErrors()){
            model.addAttribute("travel", travel);
            return "travel/travel_detail";
        }
        this.replyService.create(travel, replyForm.getContent(), siteUser);
        return String.format("redirect:/travel/detail/%s", id);
    }
}
