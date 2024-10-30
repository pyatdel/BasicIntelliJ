package kr.or.ddit.repository;

import kr.or.ddit.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/*
CrudRepository는 JPA에서 제공하는 인터페이스로
이를 상속해 엔티티를 관리(생성, 조회, 수정, 삭제)할 수 있음.
CrudRepository에 홑화살괄호(<>)를 붙이고 그 안에 다음과 같이 2개의 제네릭 요소를 받음
 */
public interface ArticleRepository extends CrudRepository<Article, Long> {
    // Iterable<T> findAll();

    // 부모 클래스의 메서드를 상속받아 재정의
    @Override
    ArrayList<Article> findAll(); // Iterable -> ArrayList 수정
}
