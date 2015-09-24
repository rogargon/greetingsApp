package cat.udl.eps.softarch.hello.handler;

import com.google.common.io.CharStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
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

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ErrorInfo handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException e) {
        logger.info("Generating HTTP BAD REQUEST from ConstraintViolationException: {}", e.toString());
        return new ErrorInfo(HttpStatus.UNPROCESSABLE_ENTITY, request, e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ErrorInfo handleNotFound(HttpServletRequest request, Exception e) {
        logger.info("Generating HTTP NOT FOUND from NullPointerException: {}", e.toString());
        return new ErrorInfo(HttpStatus.NOT_FOUND, request, e);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ErrorInfo handleNotFound(HttpServletRequest request, ResourceNotFoundException e) {
        logger.info("Generating HTTP NOT FOUND from NullPointerException: {}", e.toString());
        return new ErrorInfo(HttpStatus.NOT_FOUND, request, e);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorInfo handleMethodArgumentNotValidException(HttpServletRequest request, Exception e) {
        logger.info("Generating HTTP BAD REQUEST from MethodArgumentNotValidException: {}", e.toString());
        return new ErrorInfo(HttpStatus.BAD_REQUEST, request, e);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity handleTxException(TransactionSystemException ex) {
        Throwable t = ex.getCause();
        if(t instanceof ConstraintViolationException){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.info("Handling generic Exception: {}", e.toString());
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
            throw e;
        return new ErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR, request, e);
    }

    public class ErrorInfo {
        private int status;
        private String operation;
        private String error;
        private String message;

        public ErrorInfo(HttpStatus status, HttpServletRequest request, Exception e) {
            String requestContent = "";
            try { requestContent = CharStreams.toString(request.getReader());
            } catch (Exception ioe) { logger.error(ioe.getMessage()); }
            this.status = status.value();
            this.error = status.getReasonPhrase();
            this.operation = request.getMethod() + " " + request.getRequestURI() + " " + requestContent;
            this.message = e.getMessage();
        }

        public int getStatus() { return status; }
        public String getOperation() { return operation; }
        public String getError() { return error; }
        public String getMessage() { return message; }
    }
}
