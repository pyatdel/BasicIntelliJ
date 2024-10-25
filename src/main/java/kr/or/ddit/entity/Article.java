package kr.or.ddit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


// 1. 엔티티 선언
    /*
    이 클래스가 엔티티임을 선언.
    Entity는 JPA에서 제공하는 애너테이션. 이 애너테이션이 붙은 클래스를 기반으로
    DB에 테이블이 생성됨. 테이블 이름은 클래스 이름과 동일하게 Article로 생성됨
    */
@Entity
@Data
    public class Article {
    //Id가 빨간색으로 표시되면 마우스를 올린 후 Alt + Enter를 누르고
    //  Id(jakarta.persistence)를 선택
        /*
        대푯값을 id로 선언. 대푯값은 사람으로 따지면 주민등록번호와 같음.
        Article 엔티티 중에 제목과 내용이 같은 것이 있더라도 대푯값 id로 다른 글임을 구분할 수 있음
         */
    // 3. Id : 엔티티의 대푯값 지정
    // 3. GeneratedValue : 자동 생성 기능 추가(숫자가 자동으로 매겨짐)
    @Id
    @GeneratedValue
    private Long id;

    /*
    DTO 코드를 작성할 때와 같이 title, content 필드 선언
    두 필드도 DB에서 인식할 수 있게 Column 애너테이션을 붙임
     */
    // 2. title 필드 선언, DB 테이블의 title 열과 연결됨
    @Column
    private String title;
    // 2. content 필드 선언, DB 테이블의 content 열과 연결됨
        private String content;



    public Article(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }


    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}



