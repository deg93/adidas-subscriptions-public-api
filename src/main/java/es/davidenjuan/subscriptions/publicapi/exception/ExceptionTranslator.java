package es.davidenjuan.subscriptions.publicapi.exception;

import java.time.ZonedDateTime;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

@ControllerAdvice
public class ExceptionTranslator {

    private Logger log = LoggerFactory.getLogger(ExceptionTranslator.class);

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleSubscriptionsPublicApiException(SubscriptionsPublicApiException e, NativeWebRequest request) {
        log.error("Handling exception {} ({})", e.getId(), e.toString(), e);

        // Build and return exception response
        Optional<HttpServletRequest> httpRequestOpt = Optional.ofNullable(request.getNativeRequest(HttpServletRequest.class));
        ExceptionResponse exceptionResponse =
            new ExceptionResponse(
                e.getId(),
                e.getTimestamp(),
                e.getHttpStatus().value(),
                e.getErrorType(),
                e.getMessage(),
                httpRequestOpt.map(HttpServletRequest::getRequestURI).orElse("")
            );
        return ResponseEntity.status(e.getHttpStatus()).body(exceptionResponse);
    }

    public static class ExceptionResponse {

        private String id;

        private ZonedDateTime timestamp;

        private int status;

        private String error;

        private String message;

        private String path;

        public ExceptionResponse(String id, ZonedDateTime timestamp, int status, String error, String message, String path) {
            this.id = id;
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public ZonedDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(ZonedDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("ExceptionResponse [id=");
            builder.append(id);
            builder.append(", timestamp=");
            builder.append(timestamp);
            builder.append(", status=");
            builder.append(status);
            builder.append(", error=");
            builder.append(error);
            builder.append(", message=");
            builder.append(message);
            builder.append(", path=");
            builder.append(path);
            builder.append("]");
            return builder.toString();
        }
    }
}
