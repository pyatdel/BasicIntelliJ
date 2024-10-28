package kr.or.ddit.controller;

import kr.or.ddit.dto.ArticleForm;
import kr.or.ddit.entity.Article;
import kr.or.ddit.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

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

    articleRerpository : JPA에서 제공해주는 리파지터리. 리파지터리와 함께 다니는 애는
                         엔티티(Article), DB
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

    //@ResponseBody
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

        // 리다이렉트는 클라이언트의 요청을 받아 새로운 URL 주소로 재요청하라고
        // 클라이언트에게 지시하는 것
        // 형식 : "redirect:URL_주소"
        return "redirect:/articles/"+saved.getId(); // 상세 페이지

    }

    /**
     요청URI : /articles/1
     /articles/2
     /articles/3
     경로변수(PathVariable) : 1
     요청방식 : get
     */
    @GetMapping("/articles/{id}") // /의 뒤의 값이 value 값과 같아야 함

    //                              경로 상의 변하는 수 : id
    public String show(@PathVariable(value="id") Long id,
                    Model model){ // 매개변수로 id 받아오기
        // id를 잘 받아쓴지 확인하는 로그 찍기
        // show-> id : 1
        log.info("show->id : "+id);

        // 1. id를 조회해 데이터 가져오기
        // findById()는 JPA의 CrudRepository가 제공하는 메서드로, 특정 엔티티의 id 값을 기준으로
        //  데이터를 찾아 Optional 타입으로 반환.
        // orElse(null) : id 값으로 데이터를 찾을 때 해당 id 값이 없으면 null을 반환.
        // 데이터를 조회한 결과, 값이 있으면 articleEntity 변수에 값을 넣고 없으면
        //  null을 저장                DB.util.selectOne(id)
        Article articleEntity = articleRepository.findById(id).orElse(null);


        // 2. 모델에 데이터 등록하기
        // article이라는 이름으로 value인 articleEntity 객체 추가
        model.addAttribute("article", articleEntity);

        // 3. 뷰 페이지 반환하기
        // 뷰 페이지는 articles라는 디렉터리 안에 show라는 파일이 있다는 의미
        // forwarding : mustache
        return "articles/show";
    }

    /*
    요청 URI : /articles
    요청 파라미터 :
    요청 방식 : get
     */
    @GetMapping("/articles")
    public String index(Model model){
        // p.134
        // 1. 모든 데이터(DB, 레퍼지터리, 엔티티)를 가져오기
        // findAll() 메서드의 반환 타입은 Iterable. List로 받아야 함.
        // 캐스팅(형변환)이 필요. Iterable->Collection->List 인터페이스의 상하 관계
        ArrayList<Article> articleEntityList = (ArrayList<Article>) this.articleRepository.findAll();  // Iterator

         /* [는 List를 말함, {는 엔티티(Article)/1행
        [
            Article{id=1, title='1', content='11'},
            Article{id=2, title='2', content='22'},
            Article{id=3, title='3', content='33'}
        ]
         */
        log.info("index->articleEntityList: "+articleEntityList);



        // 2. 모델에 데이터를 등록하기
        model.addAttribute("articleList", articleEntityList);

        // 3. 뷰페이지 설정하기(forwarding)
        // articles 디렉터리 안에 index.mustache 파일이 목록의 뷰 페이지로 설정
        // forwarding : mustache를 리턴
        return "articles/index";
    }
    /*
    요청 URI : /articles/1/edit
    경로 변수 : 1
    요청 방식 : get

    // 1. URL 주소에 있는 id를 받아오는 것임
    // 데이터 타입 앞에 골뱅이PathVariable 애너테이션을 추가
     */
    @GetMapping("/articles/{id}/edit") // 위의 값과 아래의 value 값은 같아야 함
    public String edit(@PathVariable(value = "id") Long id
            , Model model){
        // 2. DB(리파지터리, 엔티티)에서 수정할 데이터 가져옴
        // DB에서 데이터를 가져올 때는 리파지터리를 이용
        // 만약 데이터를 찾지 못하면 null을 반환, 데이터를
        // 찾았다면 Article 타입의 articleEntity로 작성
        Article articleEntity = this.articleRepository.findById(id).orElse(null);
        log.info("edit->articleEntity : " + articleEntity);

        // 3. 모델에 데이터 등록하기
        model.addAttribute("article",articleEntity);

        // 뷰 페이지 forwarding
        // templates/ + articles/edit.mustache
        return "articles/edit";
    }
}
