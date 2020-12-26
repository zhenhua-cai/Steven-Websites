package net.stevencai.blog.backend.api;

import io.jsonwebtoken.ExpiredJwtException;
import net.stevencai.blog.backend.response.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ExpiredJwtException.class)
    public AuthResponse handle401(){
        return new AuthResponse(HttpStatus.UNAUTHORIZED.value(),null,"Invalid JWT");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public AuthResponse authFail(){
        return new AuthResponse(HttpStatus.UNAUTHORIZED.value(),null,"Invalid Username/Password");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public AuthResponse globalException(){
        return new AuthResponse(HttpStatus.BAD_REQUEST.value(),null,"Unknown Errors");
    }
}
