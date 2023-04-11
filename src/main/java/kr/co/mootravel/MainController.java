package kr.co.mootravel;

import kr.co.mootravel.Like.LikeService;
import kr.co.mootravel.Travel.Travel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
