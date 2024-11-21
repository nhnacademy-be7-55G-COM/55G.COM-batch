package shop.s5g.batch.repository.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.batch.entity.coupon.CouponTemplate;
import shop.s5g.batch.repository.coupon.qdsl.CouponTemplateQuerydslRepository;

public interface CouponTemplateRepository extends JpaRepository<CouponTemplate, Long>,
    CouponTemplateQuerydslRepository {

}
