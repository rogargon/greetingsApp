package cat.udl.eps.softarch.hello.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import cat.udl.eps.softarch.hello.service.GreetingUsernameUpdateException;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@ControllerAdvice
class GlobalDefaultExceptionHandler {
    final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);
    public static final String DEFAULT_ERROR_VIEW = "error";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    ModelAndView handleConstraintViolationException(HttpServletRequest request, Exception e) {
        logger.info("Generating HTTP BAD REQUEST from ConstraintViolationException: {}", e.toString());
        return contentNegotiatedErrorView(request, e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ModelAndView handleMethodArgumentNotValidException(HttpServletRequest request, Exception e) {
        logger.info("Generating HTTP BAD REQUEST from MethodArgumentNotValidException: {}", e.toString());
        return contentNegotiatedErrorView(request, e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GreetingUsernameUpdateException.class)
    ModelAndView handleGreetingEmailUpdateException(HttpServletRequest request, Exception e) {
        logger.info("Generating HTTP BAD REQUEST from GreetingEmailUpdateException: {}", e.toString());
        return contentNegotiatedErrorView(request, e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NullPointerException.class)
    ModelAndView handleNotFound(HttpServletRequest request, Exception e) {
        logger.info("Generating HTTP NOT FOUND from NullPointerException: {}", e.toString());
        return contentNegotiatedErrorView(request, e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.info("Handling generic Exception: {}", e.toString());
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;
        return contentNegotiatedErrorView(request, e);
    }

    private ModelAndView contentNegotiatedErrorView(HttpServletRequest request, Exception e) {
        ErrorInfo errorInfo = new ErrorInfo(request.getRequestURI(), e);

        String acceptHeader = request.getHeader("Accept");
        if(acceptHeader==null || acceptHeader.contains("application/json")) {
            MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
            return new ModelAndView(jsonView).addObject(errorInfo);
        }
        return new ModelAndView(DEFAULT_ERROR_VIEW, "error", errorInfo);
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
