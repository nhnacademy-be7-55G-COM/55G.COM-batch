package shop.s5g.batch.coupon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.batch.entity.coupon.Coupon;
import shop.s5g.batch.entity.coupon.CouponPolicy;
import shop.s5g.batch.entity.coupon.CouponTemplate;
import shop.s5g.batch.repository.coupon.CouponPolicyRepository;
import shop.s5g.batch.repository.coupon.CouponRepository;
import shop.s5g.batch.repository.coupon.CouponTemplateRepository;

@DataJpaTest
@ActiveProfiles("test")
class CouponCreateTest {

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;
    @Autowired
    private CouponTemplateRepository couponTemplateRepository;
    @Autowired
    private CouponRepository couponRepository;

    private static final String BASE62 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final List<Coupon> couponList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        CouponPolicy couponPolicy = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.3"))
            .condition(30000L)
            .maxPrice(null)
            .duration(30)
            .build();

        couponPolicyRepository.save(couponPolicy);

        CouponTemplate couponTemplate = new CouponTemplate(
            couponPolicy,
            "Birth Coupon",
            "This is Birth Coupon"
        );

        couponTemplateRepository.save(couponTemplate);

        for (int i = 0; i <= 500000; i++) {
            couponList.add(new Coupon(
                couponTemplate,
                createUniqueCouponNumber()
            ));
        }
    }

    @Test
    @DisplayName("쿠폰 saveAll 성능 테스트")
    void testSaveAllMethod() {
        // Given
        long startTime = System.currentTimeMillis();

        // When
        couponRepository.saveAll(couponList);

        // Then
        long endTime = System.currentTimeMillis();

        System.out.println("SaveAll() 시간" + ((endTime - startTime)/1000) + "초");
    }



    /**
     * 쿠폰 번호 랜덤 생성
     * @return String
     */
    public static String createUniqueCouponNumber() {
        String uuid = UUID.randomUUID().toString();
        return encodeBase62(uuid.getBytes()).substring(0, 15);
    }

    /**
     * Base62 인코딩
     * @param input 바이트 배열
     * @return String
     */
    public static String encodeBase62(byte[] input) {
        StringBuilder result = new StringBuilder();
        for (byte b : input) {
            result.append(BASE62.charAt(((b & 0xFF) % BASE62.length())));
        }
        return result.toString();
    }
}
