package kr.co.mootravle;

import kr.co.mootravle.Question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MootravleApplicationTests {

	@Autowired
	private QuestionService questionService;

	@Test
	void testJpa() {
		for (int i = 1; i <=300; i++){
			String subject = String.format("테스트 데이터 입니다:[%03d]", i);

			String content = "내용무";
//			this.questionService.create(subject,content, null);
		}
	}

}
