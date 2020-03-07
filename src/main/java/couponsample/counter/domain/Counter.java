package couponsample.counter.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

import static java.util.Objects.nonNull;

/**
 * Global Atomic Long
 */
@ToString
@Getter
@EqualsAndHashCode(of = "id", callSuper = false)
public class Counter {

    @NotBlank
    private String id;

    private Long value;

    private Counter() {}

    private Counter(final String id, final Long value) {
        this.id = id;
        this.value = value;
    }

    public static Counter of(final String id, final Long value) {
        return new Counter(id, value);
    }

    public boolean isGreaterThanZero() {
        return nonNull(value) && 0L < value;
    }
}


