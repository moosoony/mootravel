package kr.co.mootravel;

import kr.co.mootravel.Question.QuestionService;
import kr.co.mootravel.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MootravelApplicationTests {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserRepository repo;

    @Test
    void testJpa() {
//		for (int i = 1; i <=300; i++){
//			String subject = String.format("테스트 데이터 입니다:[%03d]", i);

//			String content = "내용무";
//			this.questionService.create(subject,content, null);

//        Collection<Travel> results = repo.findByauthorId(1);
//
//        results.forEach(
//                travel -> System.out.println(travel)
//        );
    }
}