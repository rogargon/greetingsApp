package cat.udl.eps.softarch.hello.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@ControllerAdvice
class CustomExceptionHandler {
    final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ErrorInfo handleConstraintViolationException(HttpServletRequest request, Exception e) {
        logger.info("Generating HTTP BAD REQUEST from ConstraintViolationException: {}", e.toString());
        return new ErrorInfo(request.getRequestURI(), e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ErrorInfo handleNotFound(HttpServletRequest request, NullPointerException e) {
        logger.info("Generating HTTP NOT FOUND from NullPointerException: {}", e.toString());
        return new ErrorInfo(request.getRequestURI(), e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorInfo handleMethodArgumentNotValidException(HttpServletRequest request, Exception e) {
        logger.info("Generating HTTP BAD REQUEST from MethodArgumentNotValidException: {}", e.toString());
        return new ErrorInfo(request.getRequestURI(), e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.info("Handling generic Exception: {}", e.toString());
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;
        return new ErrorInfo(request.getRequestURI(), e);
    }

    public class ErrorInfo {
        private final String url;
        private final String message;

        public ErrorInfo(String url, Exception e) {
            this.url = url;
            this.message = e.getMessage();
        }

        public String getUrl() { return url; }
        public String getMessage() { return message; }
    }
}
