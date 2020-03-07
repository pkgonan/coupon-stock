package couponsample.stock.service;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class StockParameter {

    @NotNull
    @Min(1)
    private Long total;
}
