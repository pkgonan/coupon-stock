package couponsample.coupon.service;

import couponsample.common.service.EventPublisherService;
import couponsample.coupon.domain.*;
import couponsample.stock.domain.Stock;
import couponsample.stock.service.StockDto;
import couponsample.stock.service.StockParameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class CouponService extends EventPublisherService {

    private final CouponRepository repository;

    public CouponService(final CouponRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CouponDto create(final CouponParameter parameter) {
        CouponCreated event = Coupon.of(
                parameter.getName(),
                parameter.getPrice(),
                toStock(parameter.getStock())
        );

        Coupon coupon = event.getCoupon();
        repository.save(coupon);
        publishEvent(event);

        return toDto(coupon);
    }

    @Transactional(readOnly = true)
    public CouponDto get(final Long id) {
        return toDto(load(id));
    }

    @Transactional
    public CouponDto edit(final Long id, final CouponParameter parameter) {
        Coupon coupon = load(id);

        CouponEdited event = coupon.edit(parameter.getName(),
                parameter.getPrice(),
                toStock(parameter.getStock())
        );
        publishEvent(event);

        return toDto(coupon);
    }

    @Transactional
    public CouponDto delete(final Long id) {
        Coupon coupon = load(id);
        validateEditable(coupon);

        CouponDeleted event = coupon.delete();
        repository.delete(event.getCoupon());
        publishEvent(event);

        return toDto(coupon);
    }

    private void validateEditable(Coupon coupon) {
        coupon.ensureEditable();
    }

    private Stock toStock(final StockParameter parameter) {
        if (isNull(parameter)) {
            return null;
        }
        return Stock.of(parameter.getTotal());
    }

    private Coupon load(final Long id) {
        return repository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    private CouponDto toDto(final Coupon coupon) {
        CouponDto dto = new CouponDto();
        dto.setId(coupon.getId());
        dto.setName(coupon.getName());
        dto.setPrice(coupon.getPrice());
        dto.setState(coupon.getState().name());
        dto.setStock(toDto(coupon.getStock()));

        return dto;
    }

    private StockDto toDto(final Optional<Stock> stockOptional) {
        if (!stockOptional.isPresent()) {
            return null;
        }

        Stock stock = stockOptional.get();
        StockDto dto = new StockDto();
        dto.setTotal(stock.getTotal());
        dto.setRemain(stock.getRemain());

        return dto;
    }
}
