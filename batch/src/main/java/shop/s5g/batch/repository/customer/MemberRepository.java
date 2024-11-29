package shop.s5g.batch.repository.customer;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.batch.entity.customer.Member;
import shop.s5g.batch.entity.customer.MemberStatus;
import shop.s5g.batch.repository.customer.qdsl.MemberQuerydslRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQuerydslRepository {
    List<Member> findByLatestLoginAtBeforeAndStatus(LocalDateTime latestLoginAt, MemberStatus status);
}
