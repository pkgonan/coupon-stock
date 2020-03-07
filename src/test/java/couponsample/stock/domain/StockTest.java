package couponsample.stock.domain;

import couponsample.stock.exception.InvalidStockValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StockTest {

    @Test
    void of_success() {
        Stock stock = Stock.of(1L);
        Assertions.assertEquals(1L, stock.getTotal());
        Assertions.assertEquals(1L, stock.getRemain());
    }

    @Test
    void of_failure() {
        Assertions.assertThrows(InvalidStockValueException.class, () -> Stock.of(0L));
    }

    @Test
    void increase_success() {
        Stock stock = Stock.of(1L);
        stock.increase(1L);

        Assertions.assertEquals(2L, stock.getTotal());
        Assertions.assertEquals(2L, stock.getRemain());
    }

    @Test
    void increase_success_after_usage() {
        Stock stock = Stock.of(1L);
        stock.syncCurrent(0L);
        stock.increase(1L);

        Assertions.assertEquals(2L, stock.getTotal());
        Assertions.assertEquals(1L, stock.getRemain());
    }

    @Test
    void increase_failure() {
        Stock stock = Stock.of(1L);
        Assertions.assertThrows(InvalidStockValueException.class, () -> stock.increase(0L));
    }

    @Test
    void increase_overflow_failure() {
        Stock stock = Stock.of(Long.MAX_VALUE);
        Assertions.assertThrows(InvalidStockValueException.class, () -> stock.increase(1L));
    }

    @Test
    void decrease_success() {
        Stock stock = Stock.of(2L);
        stock.decrease(1L);

        Assertions.assertEquals(1L, stock.getTotal());
        Assertions.assertEquals(1L, stock.getRemain());
    }

    @Test
    void decrease_success_after_usage() {
        Stock stock = Stock.of(2L);
        stock.syncCurrent(0L);
        stock.decrease(1L);

        Assertions.assertEquals(1L, stock.getTotal());
        Assertions.assertEquals(-1L, stock.getRemain());
    }

    @Test
    void decrease_failure() {
        Stock stock = Stock.of(1L);
        Assertions.assertThrows(InvalidStockValueException.class, () -> stock.decrease(0L));
    }

    @Test
    void decrease_minimum_value_failure() {
        Stock stock = Stock.of(1L);
        Assertions.assertThrows(InvalidStockValueException.class, () -> stock.decrease(1L));
    }

    @Test
    void syncCurrent() {
        Stock stock = Stock.of(1L);
        stock.syncCurrent(0L);

        Assertions.assertEquals(1L, stock.getTotal());
        Assertions.assertEquals(0L, stock.getRemain());
    }
}