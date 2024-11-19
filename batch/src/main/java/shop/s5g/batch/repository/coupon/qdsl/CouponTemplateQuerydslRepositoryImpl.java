package shop.s5g.batch.repository.coupon.qdsl;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.batch.entity.coupon.CouponTemplate;
import shop.s5g.batch.entity.coupon.QCouponTemplate;

/**
 * 쿠폰 템플릿 쿼리 dsl
 */
public class CouponTemplateQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CouponTemplateQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CouponTemplateQuerydslRepositoryImpl(EntityManager em) {
        super(CouponTemplate.class);
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;

    /**
     * 특정 쿠폰 템플렛이 존재하는 지 체크
     * @param keyword
     * @return CouponTemplate
     */
    @Override
    public CouponTemplate findParticularCouponTemplateByName(String keyword) {

        return jpaQueryFactory
            .selectFrom(couponTemplate)
            .where(Expressions.stringTemplate("locate({0}, {1})", keyword, couponTemplate.couponName).gt("0"))
            .fetchOne();
    }
}
