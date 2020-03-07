package couponsample.coupon.domain;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class CouponStockEventHandler {

    private final CouponStockHandler stockHandler;

    public CouponStockEventHandler(final CouponStockHandler handler) {
        this.stockHandler = handler;
    }

    @EventListener
    public void onCouponStockIncreased(final CouponStockIncreased event) {
        final Coupon coupon = event.getCoupon();
        final long value = event.getValue();

        stockHandler.safeIncrease(coupon, value);
    }

    @EventListener
    public void onCouponStockDecreased(final CouponStockDecreased event) {
        final Coupon coupon = event.getCoupon();
        final long value = event.getValue();

        stockHandler.safeDecrease(coupon, value);
    }
}