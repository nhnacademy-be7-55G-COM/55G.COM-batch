package shop.s5g.batch.repository.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.batch.entity.coupon.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
