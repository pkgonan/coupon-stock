package couponsample.stock.domain;

import couponsample.stock.exception.InvalidStockValueException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
public final class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Long total;

    @NotNull
    @Column(nullable = false)
    private Long remain;

    Stock() {}

    public static Stock of(final long total) {
        ensureValidStock(total);

        final Stock stock = new Stock();
        stock.total = total;
        stock.remain = total;

        return stock;
    }

    public final void increase(final long value) {
        ensureValidStock(value);

        final long nextTotal = total + value;
        final long nextRemain = remain + value;

        ensureValidStock(nextTotal);

        this.total = nextTotal;
        this.remain = nextRemain;
    }

    public final void decrease(final long value) {
        ensureValidStock(value);

        final long nextTotal = total - value;
        final long nextRemain = remain - value;

        ensureValidStock(nextTotal);

        this.total = nextTotal;
        this.remain = nextRemain;
    }

    public final void syncCurrent(final long value) {
        this.remain = value;
    }

    private static void ensureValidStock(final long value) {
        if (value < 1L) {
            throw new InvalidStockValueException();
        }
    }
}



