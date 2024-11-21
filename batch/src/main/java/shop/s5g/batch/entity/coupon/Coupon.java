package shop.s5g.batch.entity.coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_template_id")
    private CouponTemplate couponTemplate;

    @NotBlank
    private String couponCode;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    private LocalDateTime usedAt;

    @NotNull
    private boolean active;

    public Coupon(CouponTemplate couponTemplate, String couponCode, LocalDateTime expiredAt) {
        this.couponTemplate = couponTemplate;
        this.couponCode = couponCode;
        this.expiredAt = expiredAt;
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }
}
