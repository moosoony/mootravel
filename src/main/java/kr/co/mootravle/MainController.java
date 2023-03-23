package kr.co.mootravle;

import kr.co.mootravle.Like.Like;
import kr.co.mootravle.Like.LikeService;
import kr.co.mootravle.Reply.ReplyService;
import kr.co.mootravle.Travel.Travel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {
    private final LikeService likeService;

    @GetMapping("/")
    public String main(Model model) {

        List<Travel> destination = this.likeService.findTravelByThisMonth();

        model.addAttribute("destination",destination);

        return "index";
    }


}
