package couponsample.coupon.domain;

import lombok.Getter;

@Getter
public class CouponStockDecreased extends CouponEvent {

    private final long value;

    protected CouponStockDecreased(final Coupon coupon, final long value) {
        super(coupon);
        this.value = value;
    }
}
