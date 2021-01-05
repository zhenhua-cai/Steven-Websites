package net.stevencai.blog.backend.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.stevencai.blog.backend.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlerFilter implements Filter {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (LockedException ex) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatus(HttpStatus.LOCKED.value());
            errorResponse.setMessage(ex.getMessage());

            ((HttpServletResponse) servletResponse).setStatus(HttpStatus.LOCKED.value());
            servletResponse.getWriter().write(convertObjectToJson(errorResponse));
        } catch (RuntimeException ex) {
            logger.error(ex.getMessage(), ex);

            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.setMessage("Unknown Server Error");

            ((HttpServletResponse) servletResponse).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            servletResponse.getWriter().write(convertObjectToJson(errorResponse));
        }
    }
}
