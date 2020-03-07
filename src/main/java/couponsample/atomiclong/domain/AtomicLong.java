package couponsample.atomiclong.domain;

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
public class AtomicLong {

    @NotBlank
    private String id;

    private Long value;

    private AtomicLong() {}

    private AtomicLong(final String id, final Long value) {
        this.id = id;
        this.value = value;
    }

    public static AtomicLong of(final String id, final Long value) {
        return new AtomicLong(id, value);
    }

    public boolean isGreaterThanZero() {
        return nonNull(value) && 0L < value;
    }
}


