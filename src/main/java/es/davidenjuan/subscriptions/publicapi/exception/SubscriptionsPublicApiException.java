package es.davidenjuan.subscriptions.publicapi.exception;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public abstract class SubscriptionsPublicApiException extends RuntimeException {

    private final ZonedDateTime timestamp;

    private final String id;

    protected SubscriptionsPublicApiException() {
        super();
        this.timestamp = ZonedDateTime.now();
        this.id = UUID.randomUUID().toString();
    }

    protected SubscriptionsPublicApiException(String message, Throwable cause) {
        super(message, cause);
        this.timestamp = ZonedDateTime.now();
        this.id = UUID.randomUUID().toString();
    }

    protected SubscriptionsPublicApiException(String message) {
        super(message);
        this.timestamp = ZonedDateTime.now();
        this.id = UUID.randomUUID().toString();
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public abstract HttpStatus getHttpStatus();

    public abstract String getErrorType();
}
