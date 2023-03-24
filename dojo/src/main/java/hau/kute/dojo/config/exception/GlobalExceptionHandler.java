package hau.kute.dojo.config.exception;

import hau.kute.dojo.exception.DojoException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Objects;

@Slf4j(topic = "GLOBAL_EXCEPTION_HANDLER")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String TRACE = "trace";

    @Value("${reflectoring.trace:false}")
    private boolean printStackTrace;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(Exception exception, WebRequest request) {
        return buildErrorResponse(exception, request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception ex, WebRequest request) {
        if (ex instanceof DojoException) {
            return handleDojoCaughtException((DojoException) ex, request);
        } else if (ex instanceof RuntimeException) {
            return handleRuntimeCaughtException((RuntimeException) ex, request);
        }
        return handleUnknownCaughtException(ex, request);
    }

    private ResponseEntity<Object> handleUnknownCaughtException(Exception exception, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return buildErrorResponse(exception, String.valueOf(status.value()), exception.getMessage(), status, request);
    }

    protected ResponseEntity<Object> handleRuntimeCaughtException(RuntimeException exception, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return buildErrorResponse(exception, String.valueOf(status.value()), exception.getMessage(), status, request);
    }

    @ExceptionHandler(DojoException.class)
    public ResponseEntity<Object> handleDojoCaughtException(DojoException exception, WebRequest request) {
        return buildErrorResponse(exception, exception.getCode(), exception.getMessage(), HttpStatus.BAD_REQUEST,
                request);
    }

    private ResponseEntity<Object> buildErrorResponse(Exception exception,
                                                      String code,
                                                      String message,
                                                      HttpStatus httpStatus,
                                                      WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setCode(code);
        errorResponse.setMessage(message);
        if (printStackTrace && isTraceOn(request)) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private boolean isTraceOn(WebRequest request) {
        String[] value = request.getParameterValues(TRACE);
        return Objects.nonNull(value)
                && value.length > 0
                && value[0].contentEquals("true");
    }
}
