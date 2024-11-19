package shop.s5g.batch.entity.coupon;

import lombok.Getter;

@Getter
public enum CouponTemplateType {
    WELCOME("Welcome"),
    BIRTH("Birth");

    private final String typeName;

    private CouponTemplateType(String typeName) {
        this.typeName = typeName;
    }
}
