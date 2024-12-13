package shop.s5g.batch.service.coupon.impl;

import java.time.LocalDateTime;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.batch.entity.coupon.Coupon;
import shop.s5g.batch.entity.coupon.UserCoupon;
import shop.s5g.batch.entity.customer.Member;
import shop.s5g.batch.repository.coupon.UserCouponRepository;
import shop.s5g.batch.service.coupon.UserCouponService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final UserCouponRepository userCouponRepository;

    /**
     * 생일 쿠폰 생성
     * @param member
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createBirthCoupon(Member member, Coupon coupon) {

        LocalDateTime now = LocalDateTime.now();
        YearMonth yearMonth = YearMonth.from(now);
        LocalDateTime lastDayOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        userCouponRepository.save(new UserCoupon(member, coupon, now, lastDayOfMonth));
    }
}
