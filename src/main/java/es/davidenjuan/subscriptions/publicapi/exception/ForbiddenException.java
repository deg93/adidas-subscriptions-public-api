package es.davidenjuan.subscriptions.publicapi.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class ForbiddenException extends SubscriptionsPublicApiException {

    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }

    @Override
    public String getErrorType() {
        return "FORBIDDEN";
    }
}