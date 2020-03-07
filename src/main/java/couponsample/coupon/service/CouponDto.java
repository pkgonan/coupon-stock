package couponsample.coupon.service;

import couponsample.stock.service.StockDto;
import lombok.Data;

@Data
public class CouponDto {
    private Long id;
    private String name;
    private Long price;
    private String state;
    private StockDto stock;
}
