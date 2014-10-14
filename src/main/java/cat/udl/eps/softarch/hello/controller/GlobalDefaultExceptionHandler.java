package cat.udl.eps.softarch.hello.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

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
        logger.info("Generating HTTP BAD REQUEST from ConstraintViolationException: {}", e);
        return contentNegotiatedErrorView(request, e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ModelAndView handleMethodArgumentNotValidException(HttpServletRequest request, Exception e) {
        logger.info("Generating HTTP BAD REQUEST from MethodArgumentNotValidException: {}", e);
        return contentNegotiatedErrorView(request, e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TransactionSystemException.class)
    ModelAndView handleTransactionException(HttpServletRequest request, Exception e) throws Throwable {
        logger.info("Processing TransactionSystemException, throwing nested exception: {}", e);
        if(e.getCause() instanceof RollbackException)
            if (e.getCause().getCause() instanceof ConstraintViolationException)
                return handleConstraintViolationException(request, (Exception) e.getCause().getCause());
        return contentNegotiatedErrorView(request, (Exception) e.getCause());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NullPointerException.class)
    ModelAndView handleNotFound(HttpServletRequest request, Exception e) {
        logger.info("Generating HTTP NOT FOUND from NullPointerException: {}", e);
        return contentNegotiatedErrorView(request, e);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.info("Handling generic Exception: {}", e);
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
