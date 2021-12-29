package es.davidenjuan.subscriptions.publicapi.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends SubscriptionsPublicApiException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getErrorType() {
        return "RESOURCE_NOT_FOUND";
    }

}
