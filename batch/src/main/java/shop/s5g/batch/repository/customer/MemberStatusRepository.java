package shop.s5g.batch.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.batch.entity.customer.MemberStatus;

public interface MemberStatusRepository extends JpaRepository<MemberStatus, Long> {
    MemberStatus findByTypeName(String typeName);
}
