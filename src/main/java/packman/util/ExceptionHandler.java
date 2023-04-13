package packman.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ResponseNonDataMessage> handleCustomException(Exception e) {
        if (e instanceof CustomException) {
            logger.error(((CustomException) e).getResponseCode().getMessage());
            return ResponseNonDataMessage.toResponseEntity(((CustomException) e).getResponseCode());
        } else if (e instanceof ConstraintViolationException || e instanceof MethodArgumentNotValidException || e instanceof HttpMessageNotReadableException) {
            return ResponseNonDataMessage.toResponseEntity(new CustomException(ResponseCode.NULL_VALUE).getResponseCode());
        }
        logger.error(e.getMessage());
        return ResponseNonDataMessage.toResponseEntity(new CustomException(ResponseCode.BAD_REQUEST).getResponseCode());
    }
}
