package es.davidenjuan.subscriptions.publicapi.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class UserAlreadySubscribedException extends SubscriptionsPublicApiException {

    public UserAlreadySubscribedException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }

    @Override
    public String getErrorType() {
        return "USER_ALREADY_SUBSCRIBED";
    }
}