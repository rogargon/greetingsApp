package cat.udl.eps.softarch.hello.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

/**
 * Created by roberto on 13/09/14.
 */

@ControllerAdvice
class GlobalDefaultExceptionHandler {
    final Logger logger = LoggerFactory.getLogger(GreetingController.class);
    public static final String DEFAULT_ERROR_VIEW = "error";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    ModelAndView handleBadRequest(HttpServletRequest request, Exception e) {
        logger.info("Generating HTTP BAD REQUEST from ConstraintViolationException: {}", e);
        return new ModelAndView(DEFAULT_ERROR_VIEW, "error", new ErrorInfo(request.getRequestURI(), e));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NullPointerException.class)
    ModelAndView handleNotFound(HttpServletRequest request, Exception e) {
        logger.info("Generating HTTP NOT FOUND from NullPointerException: {}", e);
        return new ModelAndView(DEFAULT_ERROR_VIEW, "error", new ErrorInfo(request.getRequestURI(), e));
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.info("Handling generic Exception: {}", e);
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;
        return new ModelAndView(DEFAULT_ERROR_VIEW, "error", new ErrorInfo(request.getRequestURI(), e));
    }

    public class ErrorInfo {
        private final String url;
        private final String message;

        public ErrorInfo(String url, Exception e) {
            this.url = url;
            this.message = e.getLocalizedMessage();
        }

        public String getUrl() { return url; }
        public String getMessage() { return message; }
    }
}