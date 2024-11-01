package kr.or.ddit.dto;

import kr.or.ddit.entity.Article;
import lombok.Data;

//자바빈 클래스(프로퍼티, 기본생성자, getter/setter메서드)
// PoJo : Plain Oriented Java Object(본연의 자바를 쓰자)

@Data
public class ArticleForm {
    // id 프로퍼티(필드) 추가
    private Long id;

    //제목을 받을 필드
    private String title;
    //내용을 받을 필드
    private String content;

    //기본생성자
    public ArticleForm() {}

    // 생성자
    public ArticleForm(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    /*
    //getter/setter 메서드
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //데이터를 잘 받았는지 확인하기 위한 메서드
    @Override
    public String toString() {
        return "ArticleForm{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
     */

    /*
    toEntity() 메서드에서는 폼 데이터를 담은 DTO 객체를 엔티티로 반환함.
    (return new Article();).
    전달받은 Article 클래스의 생성자 형식에 맞게 작성하면 됨.
    Article.java에서 생성자를 확인해보면 id, title, content를 매개변수로 받고 있음.
    아직 ArticleForm 객체에 id 정보는 없으므로 첫 번째 전달값은 null,
    두 번째 전달값은 title, 세 번째 전달값은 content를 입력.
     */
    public Article toEntity() {
        // DTO -> ArticleForm -> form(id=1, title=개똥이의 여행1, content=즐거운 여행1)
        // form.toEntity();
        return new Article(this.id, this.title, this.content);
    }
}
