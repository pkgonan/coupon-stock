package couponsample.coupon.domain;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CouponEventHandler {

    private CouponStockHandler stockHandler;

    public CouponEventHandler(final CouponStockHandler stockHandler) {
        this.stockHandler = stockHandler;
    }

    @EventListener
    public void onCouponStatusChanged(final CouponStateChanged event) {
        final Coupon coupon = event.getCoupon();
        if (!Coupon.State.WAIT.equals(coupon.getState())) {
           return;
        }

        coupon.getStock().ifPresent(stock -> stockHandler.create(coupon));
    }
}