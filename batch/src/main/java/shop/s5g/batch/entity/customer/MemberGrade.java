package shop.s5g.batch.entity.customer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberGradeId;

    private String gradeName;

    private int gradeCondition;

    private int point;

    private boolean active;

    public MemberGrade(String gradeName, int gradeCondition, int point, boolean active) {
        this.gradeName = gradeName;
        this.gradeCondition = gradeCondition;
        this.point = point;
        this.active = active;
    }
}
