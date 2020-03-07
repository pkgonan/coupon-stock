package couponsample.common.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import javax.annotation.Nonnull;
import java.util.Collection;

public abstract class EventPublisherService implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(@Nonnull final ApplicationEventPublisher applicationEventPublisher) {
        eventPublisher = applicationEventPublisher;
    }

    protected void publishEvent(final Object event) {
        eventPublisher.publishEvent(event);
    }

    protected void publishEvents(final Collection<?> events) {
        events.forEach(this::publishEvent);
    }
}
