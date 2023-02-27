package kr.co.mootravle.Travel;

import kr.co.mootravle.User.SiteUser;
import kr.co.mootravle.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/travel")
@RequiredArgsConstructor
@Controller
public class TravelController {

    private final UserService userService;
    private final TravelService travelService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Travel> paging = this.travelService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw",kw);
        return "travel/travel_list";
    }

    @GetMapping(value = "/detail/{id}")
        public String detail(Model model, @PathVariable("id") Integer id){
        Travel travel=this.travelService.getTravel(id);
        model.addAttribute("travel", travel);
            return "travel/travel_detail";
        }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String travelCreate(TravelForm travelForm) {
        return "travel/travel_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String travelCreate(@Valid TravelForm travelForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "travel/travel_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.travelService.create(travelForm.getSubject(), travelForm.getContent(), siteUser, travelForm.getTravelStart(), travelForm.getTravelEnd(), travelForm.getExpenses());
        return "redirect:/travel/list";
    }
}