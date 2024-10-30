package kr.or.ddit.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// REST(요청 URI,  요청 방식,  FURD) API(인터페이스)용 컨트롤러
@RestController
public class FirstApiController {

    // 요청 URI : /api/hello
    // ResponseBodt 애너테이션 생략
    @GetMapping("/api/hello")
    public String hello(){
        // 문자열 반환
        return "hello world!";
    }
}
