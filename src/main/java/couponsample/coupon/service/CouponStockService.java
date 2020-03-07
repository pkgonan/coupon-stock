package couponsample.coupon.service;

import couponsample.common.service.EventPublisherService;
import couponsample.coupon.domain.*;
import couponsample.coupon.exception.StockNotExistException;
import couponsample.stock.domain.Stock;
import couponsample.stock.service.StockDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CouponStockService extends EventPublisherService {

    private final CouponRepository repository;
    private final CouponStockHandler couponStockHandler;

    CouponStockService(final CouponRepository repository,
                       final CouponStockHandler couponStockHandler) {
        this.repository = repository;
        this.couponStockHandler = couponStockHandler;
    }

    @Transactional
    public StockDto syncStock(final long couponId) {
        final Coupon coupon = loadCoupon(couponId);
        ensureNotEditable(coupon);
        ensureStockExist(coupon);

        final Optional<Long> optionalCurrentRemain = couponStockHandler.getCurrentRemain(coupon);
        optionalCurrentRemain.ifPresent(coupon::syncCurrentStock);

        return toDto(coupon.getStock());
    }

    @Transactional
    public StockDto increaseStockLimit(final long couponId, final long value) {
        final Coupon coupon = loadCoupon(couponId);
        ensureNotEditable(coupon);
        ensureStockExist(coupon);

        final CouponStockIncreased event = coupon.increaseStockLimit(value);

        publishEvent(event);
        return toDto(event.getCoupon().getStock());
    }

    @Transactional
    public StockDto decreaseStockLimit(final long couponId, final long value) {
        final Coupon coupon = loadCoupon(couponId);
        ensureNotEditable(coupon);
        ensureStockExist(coupon);

        final CouponStockDecreased event = coupon.decreaseStockLimit(value);

        publishEvent(event);
        return toDto(event.getCoupon().getStock());
    }

    private void ensureNotEditable(final Coupon coupon) {
        coupon.ensureNotEditable();
    }

    private void ensureStockExist(final Coupon coupon) {
        coupon.ensureStockExist();
    }

    private Coupon loadCoupon(final long couponId) {
        return repository.findById(couponId).orElseThrow(NoSuchElementException::new);
    }

    private StockDto toDto(final Optional<Stock> stockOptional) {
        final Stock stock = stockOptional.orElseThrow(StockNotExistException::new);
        return toDto(stock);
    }

    private StockDto toDto(final Stock stock) {
        StockDto dto = new StockDto();
        dto.setTotal(stock.getTotal());
        dto.setRemain(stock.getRemain());

        return dto;
    }
}