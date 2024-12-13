package shop.s5g.batch.service.coupon.impl;

import java.security.SecureRandom;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.batch.entity.coupon.Coupon;
import shop.s5g.batch.entity.coupon.CouponTemplate;
import shop.s5g.batch.entity.coupon.CouponTemplateType;
import shop.s5g.batch.exception.coupon.CouponTemplateNotFoundException;
import shop.s5g.batch.repository.coupon.CouponRepository;
import shop.s5g.batch.repository.coupon.CouponTemplateRepository;
import shop.s5g.batch.service.coupon.CouponService;
import shop.s5g.batch.util.coupon.CouponUtil;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponTemplateRepository couponTemplateRepository;
    private final CouponRepository couponRepository;

    @Override
    public Coupon createBirthCoupon() {
        CouponTemplate birthTemplate = couponTemplateRepository.findParticularCouponTemplateByName(
            CouponTemplateType.BIRTH.name()
        );

        if (Objects.isNull(birthTemplate)) {
            throw new CouponTemplateNotFoundException("해당 쿠폰 템플릿이 존재하지 않습니다.");
        }

        return couponRepository.save(
            new Coupon(
                birthTemplate,
                CouponUtil.createUniqueCouponNumber()
            )
        );
    }
}
