package couponsample.coupon.domain;

import lombok.Getter;

@Getter
public class CouponStockIncreased extends CouponEvent {

    private final long value;

    protected CouponStockIncreased(final Coupon coupon, final long value) {
        super(coupon);
        this.value = value;
    }
}
