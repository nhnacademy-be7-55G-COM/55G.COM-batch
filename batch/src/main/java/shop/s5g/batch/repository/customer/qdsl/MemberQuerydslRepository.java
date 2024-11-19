package shop.s5g.batch.repository.customer.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.batch.entity.customer.Member;

public interface MemberQuerydslRepository {

    Page<Member> findAllMembersByBirthMonth(String formattedMonth, Pageable pageable);
}
