package couponsample.coupon;

import couponsample.coupon.service.CouponStockService;
import couponsample.stock.service.StockDto;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Validated
@RestController
public class CouponStockController {

    private final CouponStockService stockService;

    CouponStockController(final CouponStockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping("/coupons/{id}/stock/sync")
    public StockDto syncStock(@PathVariable @Min(1) final Long id) {
        return stockService.syncStock(id);
    }

    @PostMapping("/coupons/{id}/stock/increase")
    public StockDto increaseStockLimit(@PathVariable @Min(1) final Long id, @RequestBody @Valid final StockLimitParameter parameter) {
        return stockService.increaseStockLimit(id, parameter.getValue());
    }

    @PostMapping("/coupons/{id}/stock/decrease")
    public StockDto decreaseStockLimit(@PathVariable @Min(1) final Long id, @RequestBody @Valid final StockLimitParameter parameter) {
        return stockService.decreaseStockLimit(id, parameter.getValue());
    }

    @Data
    private static class StockLimitParameter {
        @NotNull(message = "value.NotNull")
        @Min(value = 1, message = "value.Min")
        private Long value;
    }
}