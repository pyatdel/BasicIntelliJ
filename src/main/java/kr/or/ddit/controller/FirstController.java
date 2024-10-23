package kr.or.ddit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

// "스프링아 이거 일반 클래스가 아닌 컨트롤러야"라고 알려줌
// "어 알았어, 자바빈으로 등록해서 메모리에 미리 올려놓을게"
/*
참고] 어노테이션(annotation)이란? 소스 코드에 추가해 사용하는 메타 데이터의 일종.
메타 데이터는 프로그램에서 처리해야 할 데이터가 아니라 컴파일 및 실행 과정에서 코드를 어떻게 처리해야 할지 알려 주는 추가 정보.
자바에서 어노테이션은 앞에 @기호를 붙여 사용.
 */
@Controller
public class FirstController {

    // 메서드 작성
    // URL 요청 접수 : 클라이언트로부터 "/hi"라는 요청을 받아 접수함
    // "/hi"라는 요청을 받음과 동시에 niceToMeetYou() 메서드 수행
    // Model model : 뷰 템플릿 페이지에서 사용할 변수를 등록하기 위해 모델 객체를 매개변수로 가져옴

    @GetMapping("/hi")
    public String niceToMeetYou(Model model){
        // model 객체가 "가오리" 값을 "username" 에 연결해 웹 브라우저로 보냄
        // 모델에서 사용할 변수를 등록함. 변숫값에 따라 서로 다른 뷰 템플릿 페이지가 출력됨

        model.addAttribute("username", "가오리");

        // 메서드를 수행한 결과로 greetings.mustache 파일 반환
        // return 문에는 파일 이름만 작성하면 서버가 알아서 templates 디렉터리에 있는 해당 뷰 탭플릿
        // 페이지를 찾아 웹 브라우저로 전송함

        return "greetings";
    }

    // 요청URI : /bye
    // 요청파라미터 :
    // 요청방식 : get
    // 1. 컨트롤러는 골뱅이Controller 내부에
    // @GetMapping 어노테이션을 통해 클라이언트의 요청을 받음
    @GetMapping("/bye")
    public String seeYouNext(Model model){
        // 3. goodbye.mustache  변환
        // 반환값은 요청에 따라 보여 줄 뷰 템플릿 페이지를 적음

        model.addAttribute("nickname", "로브스터");

        return "goodbye";
    }
}