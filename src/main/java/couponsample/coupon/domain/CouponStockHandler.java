package couponsample.coupon.domain;

import couponsample.coupon.exception.StockNotExistException;
import couponsample.stock.domain.Stock;
import couponsample.stock.domain.StockHandler;
import org.springframework.stereotype.Component;

@Component
public class CouponStockHandler {

    private final StockHandler handler;

    public CouponStockHandler(final StockHandler handler) {
        this.handler = handler;
    }

    public final void increase(final Coupon coupon) {
        handler.increase(generateStockKey(coupon));
    }

    public final void decrease(final Coupon coupon) {
        handler.decrease(generateStockKey(coupon));
    }

    public final void safeIncrease(final Coupon coupon) {
        handler.safeIncrease(generateStockKey(coupon));
    }

    public final void safeDecrease(final Coupon coupon) {
        handler.safeDecrease(generateStockKey(coupon));
    }

    public final boolean tryIncrease(final Coupon coupon) {
        return handler.tryIncrease(generateStockKey(coupon));
    }

    public final boolean tryDecrease(final Coupon coupon) {
        return handler.tryDecrease(generateStockKey(coupon));
    }

    public final void safeIncrease(final Coupon coupon, final long value) {
        handler.safeIncrease(generateStockKey(coupon), value);
    }

    public final void safeDecrease(final Coupon coupon, final long value) {
        handler.safeDecrease(generateStockKey(coupon), value);
    }

    public final boolean tryIncrease(final Coupon coupon, final long value) {
        return handler.tryIncrease(generateStockKey(coupon), value);
    }

    public final boolean tryDecrease(final Coupon coupon, final long value) {
        return handler.tryDecrease(generateStockKey(coupon), value);
    }

    public final void create(final Coupon coupon) {
        final String key = generateStockKey(coupon);
        final long total = getStock(coupon).getTotal();

        handler.safeSet(key, total);
    }

    public final void delete(final Coupon coupon) {
        handler.safeRemove(generateStockKey(coupon));
    }

    public final boolean isIn(final Coupon coupon) {
        return handler.isIn(generateStockKey(coupon));
    }

    public final boolean isNotIn(final Coupon coupon) {
        return handler.isNotIn(generateStockKey(coupon));
    }

    public final long getCurrentRemain(final Coupon coupon) {
        return handler.getCurrentRemain(generateStockKey(coupon));
    }

    public static String generateStockKey(final Coupon coupon) {
        return CouponStockKeyResolver.resolve(coupon);
    }

    private static Stock getStock(final Coupon coupon) {
        return coupon.getStock().orElseThrow(StockNotExistException::new);
    }

    private static class CouponStockKeyResolver {

        private static final String PREFIX_KEY = "stocks:coupons:%s";

        public static String resolve(final Coupon coupon) {
            return String.format(PREFIX_KEY, coupon.getId());
        }
    }
}