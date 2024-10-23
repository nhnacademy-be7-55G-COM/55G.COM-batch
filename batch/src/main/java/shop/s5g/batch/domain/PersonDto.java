package shop.s5g.batch.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDto {

    private Long id;
    private String name;
    private String grade;

    public PersonDto() {

    }

    public PersonDto(Long id , String name, String grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }
}
