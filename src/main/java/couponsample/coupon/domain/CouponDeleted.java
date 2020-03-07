package couponsample.coupon.domain;

public class CouponDeleted extends CouponEvent {

    protected CouponDeleted(Coupon entity) {
        super(entity);
    }
}
