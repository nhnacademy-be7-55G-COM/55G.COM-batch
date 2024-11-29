package shop.s5g.batch.service.coupon.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.YearMonth;
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
                createCouponNumber()
            )
        );
    }

    /**
     * 쿠폰 번호 랜덤 생성
     * @return String
     */
    private String createCouponNumber() {

        final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final SecureRandom random = new SecureRandom();

        StringBuilder couponNumber = new StringBuilder(15);
        for (int i = 0; i < 15; i++) {
            int number = random.nextInt(ALPHANUMERIC.length());
            couponNumber.append(ALPHANUMERIC.charAt(number));
        }

        return couponNumber.toString();
    }
}
