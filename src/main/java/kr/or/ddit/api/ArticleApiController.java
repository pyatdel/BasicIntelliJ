package kr.or.ddit.api;

import kr.or.ddit.dto.ArticleForm;
import kr.or.ddit.entity.Article;
import kr.or.ddit.repository.ArticleRepository;
import kr.or.ddit.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 이 클래스가 REST 컨트롤러임을 선언
@Slf4j // lombok
@RestController
public class ArticleApiController {


    // DI(의존성 주입) / IoC(제어의 역전)

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;


    // GET
    @GetMapping("/api/articles")
    public List<Article> index(){
        // 메서드 수행 결과로 Article 묶음을 반환하므로
        // 반환형이 List<Article>임
        // .findAll() 메서드 : DB에 저장된 모든 Article을 가져와 반환
        // return this.articleRepository.findAll();
        return this.articleService.index();

    }

    // 요청 URI : /api/articles/1
    // GET
    // 하나의 글 가져오기. 조회하려는 게시글의 id에 따라 URL 요청이 바뀜
    @GetMapping("/api/articles/{id}")
    public Article show(@PathVariable(value = "id") Long id){
        log.info("show->id " + id);

        // 메서드 수행 결과로 단일 Article을 반환하므로 메서드의 반환형을 Article로 처리
        // return 문은 DB에서 id로 검색해 얻은 엔티티를 가져오도록 수정.
        // 만약 해당 엔티티가 없으면 null을 반환
        // URL의 id를 매개변수로 받아 오기. DB에서 id로 검색하려면 show() 메서드의 매개변수로
        // id를 받아 와야 함. 이때 id는 요청URL에서 가지고 오므로 매개변수 앞에
        // PathVariable 애너테이션을 붙임
        // return this.articleRepository.findById(id).orElse(null);
        Article article = this.articleService.show(id);

        return article;
    }

    /*
    요청 URI : /api/articles
    요청 파라미터 : JSON String{"title":"더 글로리","body":"나더 글 놀이"}
    요청 방식 : post

    REST API에서 데이터를 생성할 때는 JSON 데이터를 받아 와야 하므로
    단순히 매개변수로 dto를 쓴다고 해서 받아올 수 있는 게 아님.
    dto 매개변수 앞에 골뱅이RequestBody라는 애너테이션을 추가해 줘야 함.
    이렇게 하면 요청 시 본문(BODY)에 실어 보내는 데이터를 create() 메서드의
    매개변수로 받아 올 수 있음.
     */

    // POST(Insert)
    //반환형이 Article인 create()라는 메서드를 정의하고, 수정할 데이터를 dto 매개변수로 받아옴.
    @PostMapping("/api/articles")
    public ResponseEntity<Article> create(@RequestBody ArticleForm articleForm){
        // 이렇게 받아온 dto는 DB에서 활용할 수 있도록 엔티티로 변환해 article 변수에 넣고,
        // articleRepository를 통해 DB에 저장한 후 반환함
        //  엔티티                 DTO
        Article article = articleForm.toEntity();
        // return this.articleRepository.save(article);
        article = this.articleService.create(article);

        // 등록 성공 시 OK, 등록 실패 시 BAD_REQUEST도 데이터와 함께 응답
        return (article != null)?
                ResponseEntity.status(HttpStatus.OK).body(article):
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // PATCH(PUT)
    // PatchMapping 애너테이션으로 "/api/articles/1" 주소로 오는 URL 요청을 받음
    // 반환형이 Article인 update()라는 메서드를 정의하고 매개변수로 요청URL의 id와
    // 요청 메시지의 본문 데이터를 받아옴
    // 클라이언트 요청 오류일 경우 상태 코드 400을 반환해야 함. 그런데 update() 메서드의
    // 반환형을 단순히 Article로 하면 안 됨
    // Article을 ResponseEntity에 담아서 반환해야만 반환하는 데이터에 상태 코드를
    // 실어 보낼 수 있음
    // 요청파라미터 : JSON String{"id":1,"title":"더 글로리1","content":"나더 글 놀이1"}
    /*
    이 컨트롤러는 리파지터리와 협업해 게시판의 데이터를 CRUD(생성, 조회, 수정, 삭제)했음.
    여기에 서비스 계층을 추가해서 컨트롤러, 서비스, 리파지터리의 역할을 분업해보자.
    */

    @PatchMapping("/api/articles/{id}")
    public ResponseEntity<Article> update(@PathVariable(value="id") Long id,
                          @RequestBody ArticleForm dto){

        /*
        // 1. DTO -> 엔티티 변환하기(수정용 엔티티 생성)
        // 클라이언트에서 받은 수정 데이터가 담긴 dto를 DB에서 활용할 수 있도록
        // 엔티티로 변환해 article 변수에 저장
        Article article = dto.toEntity();

        // 중간에 실행이 잘 되는지 확인하기 위해 id와 article의 내용을 로그로 찍어 봄
        // id는 첫 번재 중괄호({})에 들어가고, article.toString()의 결과는 두 번째
        // 중괄호({})에 들어감
        // Slf4j는 Simple logging facade for Java의 약자. 로깅을 직접하지 않고
        // 로깅 라이브러리를 찾아서 사용할 수 있도록 해 줌
        log.info("id : {}, article : {}", id, article);

        // 2. 타깃 조회하기(DB에 대상 엔티티가 있는지 조회)
        // DB에서 대상 엔티티를 조회해 가져 옴. .findById(id)메서드를 통해서
        // DB에 해당 id를 가진 엔티티를 가져오되 없다면 널(null)을 반환.
        Article target = this.articleRepository.findById(id).orElse(null);


        //3. 잘못된 요청 처리하기(대상 엔티티가 없거나 수정하려믄 id가 잘못됐을 경우 처리)
        if(target == null || id != article.getId()){
            //400, 잘못된 요청 응답!
            log.info("update->잘못된 요청! id : {}, article: {} ", id, article.toString());
            //ResponseEntity 반환
            //ResponseEntity의 상태(status)에는 400 또는 HttpStatus.BAD_REQUEST를, 본문(body)에는
            //  반환할 데이터가 없으므로 null을 실어 반환함
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


        //4. 업데이트 및 정상 응답(200)하기
        // DB          변경될 값들
        target.patch(article);
        */


        // (대상 엔티티가 있으면 수정 내용으로 업데이트하고 정상 응답(200) 보내기
        // article 엔티티에 담긴 수정용 데이터를 DB에 저장 후 updated라는 이름의 변수에 저장
        // Article updated = this.articleRepository.save(target);
        Article updated = this.articleService.update(dto);
        log.info("updated : ", updated);

        // 정상 응답
        // 수정된 데이터는 ResponseEntity에 담아서 보냄. 이 때 상태(status)에는 장상 응답이므로
        // 200 또는 HttpStatus.OK를 싣고, 본문(body)에는 반환할 데이터인 updated를 실음.
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    // DELETE
    // 반환형으로 ResponseEntity에 <Article>을 실어 보내는 delete()라는 메서드를 정의하고
    // URL의 id를 매개변수로 받아 오자.
    /*
    요청 URI : /api/articles/1
    요청 파라미터 :
    요청 방식 : delete
     */

    /*
    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> delete(@PathVariable(value="id")long id){
        log.info("delete->id : {}", id);

        // 1. 대상 찾기(DB에서 대상 엔티티가 있는지 조회)
        // DB에 삭제할 대상 엔티티가 있는지 조회하고 없으면 null을 반환.
        // 반환받은 값은 target이라는 엔티티변수에 저장
        Article target = this.articleRepository.findById(id).orElse(null);


        // 2. 잘못된 요청 처리하기(대상 엔티티가 없어서 요청 자체가 잘못됐을 경우 처리)
        // target이 null이면 ResponseEntity의 상태(status)에는 BAD_REQUEST, 본문(body)에는
        // null을 실어 보냄
        if(target == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }


        // 3. 대상 삭제하기(대상 엔티티가 있으면 삭제하고 정상 응답(200) 반환)
        // 잘못된 요청이 아니라면 찾은 대상 엔티티를 삭제함. ResponseEntity의 상태(status)에는
        // HttpStatus.OK, 본문(body)에는 null을 실어 보냄
        this.articleRepository.delete(target);

        // return ResponseEntity.status(HttpStatus.OK).body(null);
        // return 문에 body(null) 대신 build()를 작성해도 됨. ResponseEntity의 build() 메서드는
        // HTTP 응답의 body가 없는 ResponseEntity 객체를 생성함. 따라서 build() 메서드로 생성된
        // 객체는 body(null)의 결과와 같음.
        return ResponseEntity.status(HttpStatus.OK).build();
    }
      바꿔보기 */

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Article> delete(@PathVariable(value="id")long id){
        log.info("delete->id : {}", id);

        // article = target
        Article deleted = this.articleService.delete(id);

        // return ResponseEntity.status(HttpStatus.OK).body(null);
        // return 문에 body(null) 대신 build()를 작성해도 됨. ResponseEntity의 build() 메서드는
        // HTTP 응답의 body가 없는 ResponseEntity 객체를 생성함. 따라서 build() 메서드로 생성된
        // 객체는 body(null)의 결과와 같음.
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
