package couponsample.coupon.service;

import couponsample.coupon.domain.Coupon;
import couponsample.stock.service.StockParameter;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CouponParameter {

    @NotBlank
    private String name;

    @NotNull
    private Long price;

    @Valid @NotNull
    private StockParameter stock;
}
