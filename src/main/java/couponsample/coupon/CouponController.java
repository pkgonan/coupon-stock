package couponsample.coupon;

import couponsample.coupon.service.CouponDto;
import couponsample.coupon.service.CouponParameter;
import couponsample.coupon.service.CouponService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Validated
@RestController
public class CouponController {

    private final CouponService couponService;

    public CouponController(final CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("/coupons")
    public CouponDto create(@RequestBody @Valid final CouponParameter parameter) {
        return couponService.create(parameter);
    }

    @GetMapping("/coupons/{id}")
    public CouponDto get(@PathVariable @Min(1) final Long id) {
        return couponService.get(id);
    }

    @PutMapping("/coupons/{id}")
    public CouponDto edit(@PathVariable @Min(1) final Long id, @RequestBody @Valid final CouponParameter parameter) {
        return couponService.edit(id, parameter);
    }

    @DeleteMapping("/coupons/{id}")
    public void delete(@PathVariable Long id) {
        couponService.delete(id);
    }
}
