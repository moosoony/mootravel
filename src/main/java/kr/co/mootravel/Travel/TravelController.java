package kr.co.mootravel.Travel;

import kr.co.mootravel.Like.LikeService;
import kr.co.mootravel.User.SiteUser;
import kr.co.mootravel.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/travel")
@RequiredArgsConstructor
@Controller
public class TravelController {
//    @Value("${api.key}")
//    private String api;

    private final UserService userService;
    private final TravelService travelService;
    private final LikeService likeService;

    @GetMapping("/list")
    public String list(ModalForm modalForm, Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {

        Page<Travel> paging = this.travelService.getList(page, kw);

        List<Travel> destination = this.likeService.findTravelByThisMonth();

        model.addAttribute("destination", destination);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);

        // 여행지 목록
        List<String> options = Arrays.asList("Option 1", "Option 2", "Option 3");
        model.addAttribute("options", options);


        return "travel/list";
    }


    // 모달창에 있는 값 PostMapping
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/list")
    public String modalInputValue(@RequestParam("result_from") String resultFrom,
                                  @RequestParam("result_to") String resultTo,
                                  @Valid ModalForm modalForm,
                                  BindingResult bindingResult,
                                  Model model) {
        System.out.println("모달창 POST 컨트롤러");

        //        유효성 검사 실패 시
//        if (bindingResult.hasErrors()) {
//            return "redirect:/travel/list";
//        }

        // 받은 값 확인
//        System.out.println("resultFrom 받은 값 확인 "+resultFrom);
//        System.out.println("resultTo 받은 값 확인 "+resultTo);

        // 달력에 입력받은 값 날짜로 변환
        LocalDate date1 = LocalDate.parse(resultFrom);
        LocalDate date2 = LocalDate.parse(resultTo);

        // 두 날짜의 차 계산 + 1
        long dayCount = date1.until(date2, ChronoUnit.DAYS) + 1;
        System.out.println("두 날짜의 차 : " + dayCount);


        // 입력한 값을 저장하거나 처리하는 코드
        model.addAttribute("subject", modalForm.getSubject());
        model.addAttribute("category", modalForm.getCategory());
        model.addAttribute("travelStart", resultFrom);
        model.addAttribute("travelEnd", resultTo);
        model.addAttribute("file", modalForm.getFile());
        model.addAttribute("dayCount", dayCount);

        // api key
//        model.addAttribute("api", api);


        return "travel/insert";
    }

    @GetMapping(value = "/insert")
    public String insert(TravelCreateForm travelCreateForm) {
        return "travel/insert";
    }


    @GetMapping(value = "/navbar")
    public String navbar(Model model) {
        long dayCount = 5;
        model.addAttribute("dayCount", dayCount);
        return "travel/navbar";
    }

    @PostMapping(value = "/insert")
    public String insert(TravelCreateForm travelCreateForm, Model model, BindingResult bindingResult,
                         Principal principal) throws IOException {
//        if (travelCreateForm.getFile().isEmpty()) {
//            String file = "썸네일은 필수항목입니다.";
//            model.addAttribute("file", file);
//            return "travel/travel_form";
//        }

//        if (bindingResult.hasErrors()) {
//            return "travel/travel_form";
//        }

        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.travelService.create(travelCreateForm.getSubject(), travelCreateForm.getContent(), siteUser, travelCreateForm.getTravelStart(), travelCreateForm.getTravelEnd());
        return "redirect:/travel/list";
    }

    //    상세보기
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, Principal principal, @PathVariable("id") Integer id) {

        // 사용자의 아이디
        SiteUser siteUser = this.userService.getUser(principal.getName());
        Long aid = (long) Math.toIntExact(siteUser.getId());

        // Travel 게시글의 아이디
        Travel travel = this.travelService.getTravel(id);
        Integer tid = travel.getId();

        // 조회수 증가
        travelService.updateviewcnt(id);

        // 좋아요 내역이 없으면
        if (likeService.getLike(aid, tid) == true) {

            // 좋아요
            String message = "정말로 추천하시겠습니까?";
            model.addAttribute("message", message);
        }

        // 좋아요 내역이 있으면
        else {
            // 좋아요 취소
            String message = "추천을 취소하시겠습니까?";
            model.addAttribute("message", message);
        }

        model.addAttribute("travel", travel);

        return "travel/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/index")
    public String index() {


        return "travel/index";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/map")
    public String map() {
        return "travel/map";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/maptest")
    public String maptest() {
        return "travel/maptest";
    }

    @PostMapping("/places")
    public void savePlace(@RequestBody Place place) {
        // Place 객체를 이용해 디비에 저장하는 코드
        System.out.println("장소" + place);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String travelCreate(TravelForm travelForm) {
        return "travel/travel_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")

    public String travelCreate(Model model, @Valid TravelForm travelForm, BindingResult bindingResult,
                               Principal principal) throws IOException {

        if (travelForm.getFile().isEmpty()) {
            String file = "썸네일은 필수항목입니다.";
            model.addAttribute("file", file);
            return "travel/travel_form";
        }

        if (bindingResult.hasErrors()) {
            return "travel/travel_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.travelService.create(travelForm.getSubject(), travelForm.getFile(), travelForm.getContent(), siteUser, travelForm.getTravelStart(), travelForm.getTravelEnd());
        return "redirect:/travel/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String travelModify(TravelForm travelForm, @PathVariable("id")
    Integer id, Principal principal) {
        Travel travel = this.travelService.getTravel(id);
        if (!travel.getAuthor().getUsername().equals(principal.getName())) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        travelForm.setSubject(travel.getSubject());
        travelForm.setContent(travel.getContent());
        travelForm.setTravelStart(travel.getTravelStart());
        travelForm.setTravelEnd(travel.getTravelEnd());
        return "travel/travel_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String travelModify(@Valid TravelForm travelForm, BindingResult bindingResult,
                               Principal principal,
                               @PathVariable("id") Integer id) throws IOException {

        if (bindingResult.hasErrors()) {
            return "travel/travel_form";
        }
        Travel travel = this.travelService.getTravel(id);
        if (!travel.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.travelService.modify(travel, travelForm.getSubject(), travelForm.getContent(), travelForm.getTravelStart(), travelForm.getTravelEnd(), travelForm.getFile());
        return String.format("redirect:/travel/detail/%s", id);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String travelDelete(Principal principal, @PathVariable("id") Integer id) {
        Travel travel = this.travelService.getTravel(id);
        if (!travel.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.travelService.delete(travel);
        return "redirect:/travel/list";
    }

}