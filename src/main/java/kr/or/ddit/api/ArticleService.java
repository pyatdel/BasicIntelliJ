package kr.or.ddit.api;

import kr.or.ddit.entity.Article;
import kr.or.ddit.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// 서버가 기동 시, 스프링이 이 클래스를 자바빈(객체)으로 등록(메모리에 올림)해서 관리해줌
@Service
public class ArticleService {

    // DI, IoC
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> index() {
        // 데이터는 리파지터리를 통해 가져오므로
        // 다음과 같이 DB에서 조회한 결과를 반환
        return this.articleRepository.findAll();
    }
}
