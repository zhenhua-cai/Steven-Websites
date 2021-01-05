package net.stevencai.blog.backend.api;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import net.stevencai.blog.backend.exception.*;
import net.stevencai.blog.backend.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({ExpiredJwtException.class})
    public ErrorResponse handle401() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setError(HttpStatus.UNAUTHORIZED.toString());
        response.setMessage("JWT Expired");
        return response;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse authFail() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setError(HttpStatus.UNAUTHORIZED.toString());
        response.setMessage("Invalid Username/Password");
        return response;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(DisabledException.class)
    public ErrorResponse accountDisabledException() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setError(HttpStatus.UNAUTHORIZED.toString());
        response.setMessage("Activation needed");
        return response;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(LockedException.class)
    public ErrorResponse accountLocked() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.LOCKED.value());
        response.setError(HttpStatus.LOCKED.toString());
        response.setMessage("Account is locked");
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ArticleNotFoundException.class)
    public ErrorResponse articleNotFoundException() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setError(HttpStatus.UNAUTHORIZED.toString());
        response.setMessage("Article Not Found");
        return response;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(TooManyAuthAttemptsException.class)
    public ErrorResponse tooManyAuthAttemptsException() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setError(HttpStatus.FORBIDDEN.toString());
        response.setMessage("You have too many attempts. Come back later");
        return response;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(TooManyVerifyAttemptsException.class)
    public ErrorResponse tooManyVerifyAttemptsException() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setError(HttpStatus.FORBIDDEN.toString());
        response.setMessage("You failed too many times");
        return response;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(IpBlockedException.class)
    public ErrorResponse ipBlockedException() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setError(HttpStatus.FORBIDDEN.toString());
        response.setMessage("IP address is blocked for several hours");
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({JwtException.class})
    public ErrorResponse handleInvalidJWT() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setError(HttpStatus.UNAUTHORIZED.toString());
        response.setMessage("Invalid JWT");
        return response;
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler({SignUpValidationFailedException.class})
    public ErrorResponse signUpValidationFailed() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setError(HttpStatus.UNAUTHORIZED.toString());
        response.setMessage("Sign up validation failed");
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public ErrorResponse globalException() {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setError(HttpStatus.UNAUTHORIZED.toString());
        response.setMessage("Unknown Error");
        return response;
    }
}
