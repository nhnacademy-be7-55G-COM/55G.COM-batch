package shop.s5g.batch.repository.coupon.qdsl;

import shop.s5g.batch.entity.coupon.CouponTemplate;

public interface CouponTemplateQuerydslRepository {

    CouponTemplate findParticularCouponTemplateByName(String keyword);
}
