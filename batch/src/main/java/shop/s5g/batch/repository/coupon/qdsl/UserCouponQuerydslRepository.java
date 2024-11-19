package shop.s5g.batch.repository.coupon.qdsl;

import shop.s5g.batch.entity.customer.Member;

public interface UserCouponQuerydslRepository {

    boolean hasBirthdayCoupon(Member member);
}
