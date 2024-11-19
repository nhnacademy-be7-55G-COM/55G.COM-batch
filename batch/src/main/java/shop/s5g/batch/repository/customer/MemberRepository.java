package shop.s5g.batch.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.batch.entity.customer.Member;
import shop.s5g.batch.repository.customer.qdsl.MemberQuerydslRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberQuerydslRepository {

}
