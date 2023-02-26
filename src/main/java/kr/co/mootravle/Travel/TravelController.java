package kr.co.mootravle.Travel;

import kr.co.mootravle.User.SiteUser;
import kr.co.mootravle.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;

@RequestMapping("/travel")
@RequiredArgsConstructor
@Controller
public class TravelController {

    private final UserService userService;
    private final TravelService travelService;

    @GetMapping("/list")
    public String list(Model model){
        return "travel/travel_list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String travelCreate(TravelForm travelForm){
        return "travel/travel_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String travelCreate(@Valid TravelForm travelForm, BindingResult bindingResult, Principal principal){
        if (bindingResult.hasErrors()){
            return "travel/travel_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.travelService.create(travelForm.getSubject(), travelForm.getContent(), siteUser);
        return "redirect:/travel/list";
    }
}
