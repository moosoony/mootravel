package kr.co.mootravel.Travel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceDetails;
import kr.co.mootravel.Like.LikeService;
import kr.co.mootravel.User.SiteUser;
import kr.co.mootravel.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequestMapping("/travel")
@RequiredArgsConstructor
@Controller
public class TravelController {

    private final UserService userService;
    private final TravelService travelService;
    private final LikeService likeService;

    // 전체 목록보기
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

        //        유효성 검사 실패 시
//        if (bindingResult.hasErrors()) {
//            return "redirect:/travel/list";
//        }

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

        return "travel/insert";
    }

    // 여행 일정 등록하기 GetMapping
    @GetMapping(value = "/insert")
    public String insert(TravelCreateForm travelCreateForm) {
        return "travel/insert";
    }

    // 여행 일정 등록하기 PostMapping
    @PostMapping(value = "/insert")
    public String insert(Model model, @Valid TravelInsertForm travelInsertForm, BindingResult bindingResult,
                         Principal principal) throws IOException {

        if (bindingResult.hasErrors()) {
            return "travel/travel_form";
        }

        SiteUser siteUser = this.userService.getUser(principal.getName());
        int index = 0;

        this.travelService.create(travelInsertForm.getSubject(), travelInsertForm.getContent(), siteUser, travelInsertForm.getTravelStart(), travelInsertForm.getTravelEnd(),
                travelInsertForm.getPlace_id());

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

        System.out.println("상세보기" + travel);

        // place_id 추출
        String place_id = travel.getPlace_id();

        // place_id 담을 list 생성
        List<String> place_idList = Arrays.asList(place_id.split(","));

        // 각 place_id에 대한 정보를 저장할 리스트 생성
        List<Map<String, String>> placeList = new ArrayList<>();

        // 각 place_id에 대한 정보를 Google Places API를 이용하여 가져옴
        for (String pid : place_idList) {
            Map<String, String> placeInfo = new HashMap<>();

            String apiKey = "AIzaSyDK3x6PWOe-7FZNKHUJsJFShioh6vgVGG8"; // 구글 맵스 API 키
            String apiUrl = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + pid + "&key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(apiUrl, String.class);

            // JSON 데이터를 객체로 변환합니다.
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
            JsonObject resultObject = jsonObject.getAsJsonObject("result");

            // 필요한 정보를 추출합니다.
            String name = resultObject.get("name").getAsString();
            String typesJson = resultObject.get("types").toString();
            typesJson = typesJson.replace("[", "");
            typesJson = typesJson.replace("]", "");
            typesJson = typesJson.replace("\"", "");
            String[] typesArr = typesJson.split(",");
            String types = String.join(", ", typesArr);
            Double ratingValue = resultObject.get("rating") != null ? resultObject.get("rating").getAsDouble() : null;
            String rating = ratingValue != null ? String.valueOf(ratingValue) : "없음";
            String address = resultObject.get("formatted_address").getAsString();
            String phone_number = resultObject.get("formatted_phone_number") == null ? "없음" : resultObject.get("formatted_phone_number").getAsString();
            Double latitude = resultObject.getAsJsonObject("geometry").getAsJsonObject("location").get("lat").getAsDouble();
            Double longitude = resultObject.getAsJsonObject("geometry").getAsJsonObject("location").get("lng").getAsDouble();

            // 각 place_id에 대한 정보를 placeInfo에 추가합니다.
            placeInfo.put("place_id", pid);
            placeInfo.put("name", name);
            placeInfo.put("types", types);
            placeInfo.put("rating", rating);
            placeInfo.put("address", address);
            placeInfo.put("phone_number", phone_number);
            placeInfo.put("latitude", latitude.toString());
            placeInfo.put("longitude", longitude.toString());

            // 각 place_id에 대한 정보를 placeList에 추가합니다.
            placeList.add(placeInfo);
        }

        // model 객체에 필요한 정보를 담아서 view로 전달합니다.
        model.addAttribute("travel", travel);
        model.addAttribute("placeList", placeList);

        return "travel/detail";
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

    // 수정하기
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/modify/{id}")
//    public String travelModify(TravelForm travelForm, @PathVariable("id") Integer id, Principal principal) {
//        Travel travel = this.travelService.getTravel(id);
//        if (!travel.getAuthor().getUsername().equals(principal.getName())) {
//
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
//        travelForm.setSubject(travel.getSubject());
//        travelForm.setContent(travel.getContent());
//        travelForm.setTravelStart(travel.getTravelStart());
//        travelForm.setTravelEnd(travel.getTravelEnd());
//        return "travel/travel_form";
//    }

    // 수정하기 수정 중
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String travelModify(TravelInsertForm travelInsertForm, @PathVariable("id") Integer id, Principal principal, Model model) {
        Travel travel = this.travelService.getTravel(id);
        System.out.println(travel);
        if (!travel.getAuthor().getUsername().equals(principal.getName())) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        travelInsertForm.setSubject(travel.getSubject());
        travelInsertForm.setContent(travel.getContent());
        travelInsertForm.setTravelStart(travel.getTravelStart());
        travelInsertForm.setTravelEnd(travel.getTravelEnd());
        travelInsertForm.setPlace_id(travel.getPlace_id());

        // place_id 추출
        String place_id = travel.getPlace_id();

        // place_id 담을 list 생성
        List<String> place_idList = Arrays.asList(place_id.split(","));

        // 각 place_id에 대한 정보를 저장할 리스트 생성
        List<Map<String, String>> placeList = new ArrayList<>();

        // 각 place_id에 대한 정보를 Google Places API를 이용하여 가져옴
        for (String pid : place_idList) {
            Map<String, String> placeInfo = new HashMap<>();

            String apiKey = "AIzaSyDK3x6PWOe-7FZNKHUJsJFShioh6vgVGG8"; // 구글 맵스 API 키
            String apiUrl = "https://maps.googleapis.com/maps/api/place/details/json?place_id=" + pid + "&key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(apiUrl, String.class);

            // JSON 데이터를 객체로 변환합니다.
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(result, JsonObject.class);
            JsonObject resultObject = jsonObject.getAsJsonObject("result");

            // 필요한 정보를 추출합니다.
            String name = resultObject.get("name").getAsString();
            String typesJson = resultObject.get("types").toString();
            typesJson = typesJson.replace("[", "");
            typesJson = typesJson.replace("]", "");
            typesJson = typesJson.replace("\"", "");
            String[] typesArr = typesJson.split(",");
            String types = String.join(", ", typesArr);
            Double ratingValue = resultObject.get("rating") != null ? resultObject.get("rating").getAsDouble() : null;
            String rating = ratingValue != null ? String.valueOf(ratingValue) : "없음";
            String address = resultObject.get("formatted_address").getAsString();
            String phone_number = resultObject.get("formatted_phone_number") == null ? "없음" : resultObject.get("formatted_phone_number").getAsString();
            Double latitude = resultObject.getAsJsonObject("geometry").getAsJsonObject("location").get("lat").getAsDouble();
            Double longitude = resultObject.getAsJsonObject("geometry").getAsJsonObject("location").get("lng").getAsDouble();

            // 각 place_id에 대한 정보를 placeInfo에 추가합니다.
            placeInfo.put("place_id", pid);
            placeInfo.put("name", name);
            placeInfo.put("types", types);
            placeInfo.put("rating", rating.toString());
            placeInfo.put("address", address);
            placeInfo.put("phone_number", phone_number);
            placeInfo.put("latitude", latitude.toString());
            placeInfo.put("longitude", longitude.toString());

            // 각 place_id에 대한 정보를 placeList에 추가합니다.
            placeList.add(placeInfo);
        }

        // model 객체에 필요한 정보를 담아서 view로 전달합니다.
        model.addAttribute("placeList", placeList);

        System.out.println("placeList"+placeList);
        return "travel/modify";
    }

//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/modify/{id}")
//    public String travelModify(@Valid TravelForm travelForm, BindingResult bindingResult,
//                               Principal principal,
//                               @PathVariable("id") Integer id) throws IOException {
//
//        if (bindingResult.hasErrors()) {
//            return "travel/travel_form";
//        }
//        Travel travel = this.travelService.getTravel(id);
//        if (!travel.getAuthor().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
//        this.travelService.modify(travel, travelForm.getSubject(), travelForm.getContent(), travelForm.getTravelStart(), travelForm.getTravelEnd(), travelForm.getFile());
//        return String.format("redirect:/travel/detail/%s", id);
//    }

    // 수정하기 수정 중
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String travelModify(@Valid TravelInsertForm travelInsertForm, BindingResult bindingResult,
                               Principal principal,
                               @PathVariable("id") Integer id) throws IOException {

        System.out.println(" // 수정하기 수정 중\n" +
                "    @PreAuthorize(\"isAuthenticated()\")\n" +
                "    @PostMapping(\"/modify/{id}\")\n" +
                "    public String travelModify");
        if (bindingResult.hasErrors()) {
            System.out.println("(bindingResult.hasErrors())");
            return "redirect:/travel/modify/{id}";
        }

        Travel travel = this.travelService.getTravel(id);

        if (!travel.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        System.out.println("수정할 때 place_id 들 어떻게 담기는지"+travelInsertForm.getPlace_id());
        this.travelService.modify(travel, travelInsertForm.getSubject(), travelInsertForm.getContent(), travelInsertForm.getTravelStart(), travelInsertForm.getTravelEnd(), travelInsertForm.getPlace_id());
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