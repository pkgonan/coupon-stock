package couponsample.coupon.domain;

import lombok.Getter;

@Getter
public class CouponStateChanged extends CouponEdited {

    private final Coupon.State oldState;

    CouponStateChanged(final Coupon entity, Coupon.State oldState) {
        super(entity);
        this.oldState = oldState;
    }
}
