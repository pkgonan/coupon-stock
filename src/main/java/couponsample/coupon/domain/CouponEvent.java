package couponsample.coupon.domain;

import lombok.Getter;

@Getter
public abstract class CouponEvent {

    private final Coupon coupon;

    protected CouponEvent(final Coupon entity) {
        this.coupon = entity;
    }
}
