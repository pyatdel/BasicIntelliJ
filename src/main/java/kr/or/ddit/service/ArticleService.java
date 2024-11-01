package kr.or.ddit.service;

import kr.or.ddit.dto.ArticleForm;
import kr.or.ddit.entity.Article;
import kr.or.ddit.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 서버가 기동 시, 스프링이 이 클래스를 자바빈(객체)으로 등록(메모리에 올림)해서 관리해줌
@Slf4j // 로그를 찍을 때 도와줌
@Service
public class ArticleService {

    // DI, IoC
    @Autowired
    private ArticleRepository articleRepository;

    // 글 목록
    public List<Article> index() {
        // 데이터는 리파지터리를 통해 가져오므로
        // 다음과 같이 DB에서 조회한 결과를 반환
        return this.articleRepository.findAll();
    }

    // 글 상세보기
    public Article show(Long id){
        Article article = this.articleRepository.findById(id).orElse(null);
        return article;
    }

    // 글 등록하기
    public Article create(Article article) {

        // 글 등록 시 id는 필요없음
        // DB가 알아서 생성하기 때문에
        if(article.getId() != null){
            return null;
        }
        article = this.articleRepository.save(article);

        return article;
    }

    // 글 수정
    public Article update(ArticleForm dto) {
        //1. DTO -> 엔티티 변환하기(수정용 엔티티 생성)
        //클라이언트에서 받은 수정 데이터가 담긴 dto를 DB에서 활용할 수 있도록
        //  엔티티로 변환해 article 변수에 저장
        Article article = dto.toEntity();

        Long id = article.getId();
        //중간에 실행이 잘 되는지 확인하기 위해 id와 article의 내용을 로그로 찍어 봄
        //id는 첫 번재 중괄호({})에 들어가고, article.toString()의 결과는 두 번째
        //  중괄호({})에 들어감
        //Slf4j는 Simple logging facade for Java의 약자. 로깅을 직접하지 않고
        //  로깅 라이브러리를 찾아서 사용할 수 있도록 해 줌
        log.info("id : {}, article : {}",id, article);

        //2. 타깃 조회하기(DB에 대상 엔티티가 있는지 조회)
        //DB에서 대상 엔티티를 조회해 가져 옴. .findById(id)메서드를 통해서
        //  DB에 해당 id를 가진 엔티티를 가져오되 없다면 널(null)을 반환.
        Article target = this.articleRepository.findById(id).orElse(null);

        //3. 잘못된 요청 처리하기(대상 엔티티가 없거나 수정하려믄 id가 잘못됐을 경우 처리)
        if(target == null || id != article.getId()){
            //400, 잘못된 요청 응답!
            log.info("update->잘못된 요청! id : {}, article: {} ", id, article.toString());
            //ResponseEntity 반환
            //ResponseEntity의 상태(status)에는 400 또는 HttpStatus.BAD_REQUEST를, 본문(body)에는
            //  반환할 데이터가 없으므로 null을 실어 반환함
            return null;
        }

        //4. 업데이트 및 정상 응답(200)하기
        //DB          변경될값들
        target.patch(article);

        Article updated = this.articleRepository.save(target);

        return updated;
    }

    // 글 삭제
    public Article delete(long id) {
        // 1. 대상 찾기(DB에서 대상 엔티티가 있는지 조회)
        // DB에 삭제할 대상 엔티티가 있는지 조회하고 없으면 null을 반환.
        // 반환받은 값은 target이라는 엔티티변수에 저장
        Article target = this.articleRepository.findById(id).orElse(null);


        // 2. 잘못된 요청 처리하기(대상 엔티티가 없어서 요청 자체가 잘못됐을 경우 처리)
        // target이 null이면 ResponseEntity의 상태(status)에는 BAD_REQUEST, 본문(body)에는
        // null을 실어 보냄
        if(target == null){
            return null;
        }


        // 3. 대상 삭제하기(대상 엔티티가 있으면 삭제하고 정상 응답(200) 반환)
        // 잘못된 요청이 아니라면 찾은 대상 엔티티를 삭제함. ResponseEntity의 상태(status)에는
        // HttpStatus.OK, 본문(body)에는 null을 실어 보냄
        this.articleRepository.delete(target);

        return target;
    }
}
