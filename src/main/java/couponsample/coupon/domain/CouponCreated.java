package couponsample.coupon.domain;

public class CouponCreated extends CouponEvent {

    protected CouponCreated(Coupon entity) {
        super(entity);
    }
}
