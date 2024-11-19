package shop.s5g.batch.repository.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.batch.entity.coupon.UserCoupon;
import shop.s5g.batch.repository.coupon.qdsl.UserCouponQuerydslRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long>,
    UserCouponQuerydslRepository {

}
