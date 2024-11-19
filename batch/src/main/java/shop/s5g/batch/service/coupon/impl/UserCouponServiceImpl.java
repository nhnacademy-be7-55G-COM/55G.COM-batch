package shop.s5g.batch.service.coupon.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.batch.entity.coupon.Coupon;
import shop.s5g.batch.entity.coupon.UserCoupon;
import shop.s5g.batch.entity.customer.Member;
import shop.s5g.batch.repository.coupon.UserCouponRepository;
import shop.s5g.batch.service.coupon.CouponService;
import shop.s5g.batch.service.coupon.UserCouponService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final CouponService couponService;
    private final UserCouponRepository userCouponRepository;

    /**
     * 생일 쿠폰 생성
     * @param member
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBirthCoupon(Member member) {

        if (userCouponRepository.hasBirthdayCoupon(member)) {
            log.warn("Member with ID {} already has a birthday coupon. Skipping.", member.getId());
            return;
        }

        Coupon coupon = couponService.createBirthCoupon();

        userCouponRepository.save(new UserCoupon(member, coupon));
    }
}
