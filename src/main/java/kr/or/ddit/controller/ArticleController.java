package kr.or.ddit.controller;

import kr.or.ddit.dto.ArticleForm;
import kr.or.ddit.entity.Article;
import kr.or.ddit.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//1. 컨트롤러 선언 : 이 파일이 컨트롤러임을 선언
@Slf4j

@Controller
public class ArticleController {

    // articleRepository객체 선언
    /*
    ArticleRepository 인터페이스의 구현 객체를 new 키워드로 만든 적이 없는데도 articleRepository 객체를 사용했음.
    자바를 배웠다면 당연히 다음과 같이 ArticleRepositoryImpl() 같은 구현체를 만들었겠죠.
    그러나 스프링 부트에서는 객체를 만들지 않아도 됨. 스프링 부트가 알아서 객체를 만들기 때문.
    골뱅이Autowired 애너테이션을 붙이면 스프링 부트가 미리 생성해 놓은 객체를 가져다가 연결해 줌.
     */

    /*
    스프링 부트에서 제공하는 애너테이션으로 이를 컨트롤러의 필드에 붙이면
    스프링 부트가 만들어 놓은 객체를 가져와 주입해 줌. 이를 의존성 주입(DI, Dependency Injection)이라고 함.
     */
    @Autowired
    private ArticleRepository articleRepository;

    //2. URL 요청 접수(/articles/new)
    //뷰 페이지를 보여 주기 위해 newArticleForm() 메서드를 추가함

    @GetMapping("/articles/new") // url 매핑(메모리로 올라감)
    public String newArticleForm(){ //3. 메서드 생성
        //4. 반환값 작성 : 뷰 페이지의 이름을 적음.
        // articles 디렉터리를 만들고 new.mustache 뷰 페이지를 추가했으므로
        // 파일 경로까지 포함해서 입력
        // forwarding : mustache
        return "articles/new";

    }
    /*
    요청 URI : /articles/create
    요청 파라미터 : request{title=제목 , content=내용}
    요청 방식 : post
     */

    //1. URL 요청 접수
    // GetMapping 대신 PostMapping 애너테이션을 사용함.
    // 뷰 페이지에서 폼 데이터를 post 방식으로 전송했으므로 컨트롤러에서 받을 때도
    //  PostMapping으로 받음. 이 때 괄호 안에는 받는 URL 주소를 넣음.
    // new.mustache에서 <form> 태그에 action="/articles/create"로 설정했음을 기억하자

    @ResponseBody
    @PostMapping("/articles/create") // 요청 URL
    public String CreateArticle(ArticleForm form){
        // 폼에서 전송한 데이터를 매개변수로 받아옴. DTO로 만든 클래스 이름이 ArticleForm이므로
        // ArticleForm 타입의 form 객체를 매개변수로 선언
        // 폼에서 전송한 데이터가 DTO에 잘 담겼는지 확인하기 위해 출력문을 추가
        // 로깅(logginf, 로그를 남김)을 사용하기 위해 클래스 명 위에 Slf4j를 사용함.
        log.info("createArticle->form(DTO는 JAVA) : "+form.toString());


        // 1. DTO를 엔티티(Article)로 변환
        Article article = form.toEntity();
        // createArticle->article(Entity는 DB) : Article{id=null, title='제목', content='내용'}
        log.info("(전)createArticle->article(Entity는 DB) : "+article.toString());
        // 2. 리파지터리로 엔티티를 DB에 저장
        Article saved = this.articleRepository.save(article);
        // createArticle->article(Entity는 DB) : Article{id=null, title='제목', content='내용'}
        log.info("(후)createArticle->article(Entity는 DB) : "+article.toString());
        return "success";

    }
}
