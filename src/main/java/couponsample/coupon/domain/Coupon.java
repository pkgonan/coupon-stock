package couponsample.coupon.domain;

import couponsample.coupon.exception.StateChangeException;
import couponsample.coupon.exception.StatusModifiableException;
import couponsample.coupon.exception.StatusNotModifiableException;
import couponsample.coupon.exception.StockNotExistException;
import couponsample.stock.domain.Stock;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.Supplier;

@ToString
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
@Table
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private State state;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, optional = true)
    private Stock stock;

    public static CouponCreated of(final String name, final Long price, final Stock stock) {
        Coupon coupon = new Coupon();
        coupon.name = name;
        coupon.price = price;
        coupon.state = State.WAIT;
        coupon.stock = stock;

        return new CouponCreated(coupon);
    }

    public CouponEdited edit(final String name, final Long price, final Stock stock) {
        ensureEditable();

        this.name = name;
        this.price = price;
        this.stock = stock;

        return new CouponEdited(this);
    }

    public CouponDeleted delete() {
        ensureEditable();

        return new CouponDeleted(this);
    }

    public CouponStateChanged on() {
        State oldState = this.state;
        this.state = this.state.turnOn();

        return new CouponStateChanged(this, oldState);
    }

    public CouponStateChanged off() {
        State oldState = this.state;
        this.state = this.state.turnOff();

        return new CouponStateChanged(this, oldState);
    }

    public boolean hasStock() {
        return getStock().isPresent();
    }

    public Optional<Stock> getStock() {
        return Optional.ofNullable(stock);
    }

    public CouponStockIncreased increaseStockLimit(final long value) {
        ensureStockExist();
        stock.increase(value);

        return new CouponStockIncreased(this, value);
    }

    public CouponStockDecreased decreaseStockLimit(final long value) {
        ensureStockExist();
        stock.decrease(value);

        return new CouponStockDecreased(this, value);
    }

    public void syncCurrentStock(final long value) {
        ensureStockExist();
        stock.syncCurrent(value);
    }

    public void ensureEditable() {
        if (this.state.isNotEditable()) {
            throw new StatusNotModifiableException();
        }
    }

    public void ensureNotEditable() {
        if (this.state.isEditable()) {
            throw new StatusModifiableException();
        }
    }

    public void ensureStockExist() {
        if (!hasStock()) {
            throw new StockNotExistException();
        }
    }

    public enum State {
        WAIT(true, State::acceptOn, State::acceptOff),
        ON(false, State::notAccept, State::acceptOff),
        OFF(false, State::acceptOn, State::notAccept);

        final boolean editable;
        final Supplier<State> turnOnFunction;
        final Supplier<State> turnOffFunction;

        State(boolean editable, Supplier<State> turnOnFunction, Supplier<State> turnOffFunction) {
            this.editable = editable;
            this.turnOnFunction = turnOnFunction;
            this.turnOffFunction = turnOffFunction;
        }

        boolean isEditable() {
            return this.editable;
        }

        boolean isNotEditable() {
            return !isEditable();
        }

        State turnOn() {
            return turnOnFunction.get();
        }

        State turnOff() {
            return turnOffFunction.get();
        }

        private static State acceptOn() {
            return State.ON;
        }

        private static State acceptOff() {
            return State.OFF;
        }

        private static State notAccept() {
            throw new StateChangeException();
        }
    }
}