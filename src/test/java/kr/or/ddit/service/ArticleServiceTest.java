package kr.or.ddit.service;

// Test 패키지 임포트
import jakarta.transaction.Transactional;
import kr.or.ddit.dto.ArticleForm;
import kr.or.ddit.entity.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// 앞으로 사용할 수 있는 패키지 임포트
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// 해당 클래스를 스프링 부트와 연동해 테스트. 이렇게 하면
// 테스트 코드에서 스프링 부트가 관리하는 다양한 객체를 주입받을 수 있음
@SpringBootTest
class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    // 해당 메서드가 테스트 코드임을 선언
    @Test
    void index() {
        // 테스트 코드..
        // 1. 예상 데이터
        // 예상 데이터 객체로 저장
        Article a = new Article(1L,"개똥이의 여행", "즐거운 여행");
        Article b = new Article(2L,"개똥이의 여행2", "즐거운 여행2");
        Article c = new Article(3L,"개똥이의 여행3", "즐거운 여행3");

        // Arrays.asList() 메서드로 합친 정적 리스트를 새 ArrayList로 만들어 expected에 저장함.
        List<Article> expected = new ArrayList<Article>(Arrays.asList(a,b,c)); // articles의 a, b, b를 Arrays라는 객체타입으로 만듦

        // 2. 실제 데이터
        // 실제 데이터는 articleService.index() 메서드를 호출해 그 결과를
        // List<Article> 타입의 articles에 받아옴.
        // 모든 게시글을 조회 요청하고 그 결과로 반환되는 게시글의 묶음을 받아 옴
        List<Article> articles = this.articleService.index();

        // 3. 비교 및 검증
        assertEquals(expected.toString(), articles.toString());

        // 여기까지 테스트 코드
    }

    // 성공 -> 성공_존재하는_id_입력
    @Test
    void show_성공_존재하는_id_입력() { // 원래 메소드명은 한글(x)
        // 1. 예상 데이터
        Long id = 1L;

        // 예상 데이터
        Article expected = new Article(id, "개똥이의 여행", "즐거운 여행");

        // 2. 실제 데이터
        Article article1 = this.articleService.show(id);

        // 3. 비교 및 검증
        assertEquals(expected.toString(), article1.toString());

    }

    // 실패 -> 실패_존재하지 않는_id_입력
    @Test
    void show_실패_존재하지_않는_id_입력() {
        // 1. 예상 데이터
        // 예상 데이터는 존재하지 않는 id인 -1을 조회한다고 가정해보자
        Long id = -1L;


        // 예상 데이터 저장
        // 이 경우 DB에서 조회되는 내용이 없어 null을 반환할 것이므로 expected 객체에 null을 저장함
        // expected 객체애 null을 저장함

        Article expected = null;


        // 2. 실제 데이터
        Article article = this.articleService.show(id);


        // 3. 비교 및 검증
        // 실제 데이터와 예상 데이터의 값 null은 toString() 메서드를 호출할 수 없으므로
        // 첫 번째와 두 번째 전달값은 expected와 article을 사용함
        assertEquals(expected, article);
    }



    // 게시글 생성(등록)
    // Transactional 애너테이션이 있으면
    // 테스트 중에 입력된 데이터가 rollback된다.
    @Test
    // 골뱅이Transactional
    void create_성공_title과_content만_있는_dto_입력() {
        // 1. 예상 데이터
        // title과 content 값 임의 등록
        String title = "개똥이의 여행5";
        String content = "즐거운 여행5";
        // dto 생성
        ArticleForm dto = new ArticleForm(null, title, content);


        // 예상 데이터
        // 예상 데이터는 사용자가 새 게시물을 생성한 상황을 가정해 작성함.
        // 그런데 id는 DB에서 자동으로 생성하므로 써 줄 필요가 없음
        // 예상 데이터의 id, title, content를 저장하는데 id는 필드를
        // 따로 선언하지 않았으므로 자동으로 생성될 값인 4L을 써줌
        Article expected = new Article(4L, title, content);


        //2. 실제 데이터
        // public Article create(Article article){
        Article article = dto.toEntity();
        article = this.articleService.create(article);

        // 3. 비교 및 검증
        // 예상 데이터와 실제 데이터를 비교해보자
        assertEquals(expected.toString(), article.toString());
    }

    @Test
    @Transactional
    void create_실패_id가_포함된_dto_입력() {
        // 1. 예상 데이터
        // id, title, content 값 임의 배정
        Long id = 4L;
        String title = "개똥이의 여행";
        String content = "즐거운 여행5";
        // ArticleForm dto = ??; // 테스트 해보기
        ArticleForm dto = new ArticleForm(id, title, content);
        Article expected = null; //예상 데이터

        // 2. 실제 데이터. 실제 생성 결과 저장
        // Article article = ??; // 테스트 해보기
        Article article = dto.toEntity();
        article = this.articleService.create(article);

        // 3. 비교 및 검증
        // id가 포함된 게시글 생성 요청이 올 경우 실제 실행 결과도 null을 반환했고,
        // 예상 데이터에서도 null을 반환했다고 했으므로 테스트에 통과한 것임
        // assertEquals(??, ??); // 테스트 해보기
        assertEquals(expected, article);

    }
}