package shop.s5g.batch.repository.coupon.qdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.batch.entity.coupon.QCoupon;
import shop.s5g.batch.entity.coupon.QCouponTemplate;
import shop.s5g.batch.entity.coupon.QUserCoupon;
import shop.s5g.batch.entity.coupon.UserCoupon;
import shop.s5g.batch.entity.customer.Member;

public class UserCouponQuerydslRepositoryImpl extends QuerydslRepositorySupport implements
    UserCouponQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public UserCouponQuerydslRepositoryImpl(EntityManager em) {
        super(UserCoupon.class);
        this.queryFactory = new JPAQueryFactory(em);
    }

    QUserCoupon userCoupon = QUserCoupon.userCoupon;
    QCoupon coupon = QCoupon.coupon;
    QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;

    /**
     * 이미 생일 쿠폰이 있는 지 체크
     * @param member
     * @return
     */
    @Override
    public boolean hasBirthdayCoupon(Member member) {

        return queryFactory
            .select(couponTemplate.couponName)
            .from(userCoupon)
            .innerJoin(coupon)
            .on(userCoupon.userCouponPk.couponId.eq(coupon.couponId))
            .innerJoin(couponTemplate)
            .on(coupon.couponTemplate.couponTemplateId.eq(couponTemplate.couponTemplateId))
            .where(userCoupon.member.eq(member)
                .and(couponTemplate.couponName.containsIgnoreCase("Birth"))
                .and(coupon.active.eq(true)))
            .fetchOne() != null;
    }
}
