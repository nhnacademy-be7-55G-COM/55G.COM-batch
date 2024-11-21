package shop.s5g.batch.repository.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.batch.entity.coupon.CouponPolicy;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long> {

}
