package io.datappeal.spring.off.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import java.io.IOException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Order(HIGHEST_PRECEDENCE)
public class InFlightRequestsFilter implements Filter, Ordered {

    private final InFlightCounter inFlightCounter;

    public InFlightRequestsFilter(InFlightCounter inFlightCounter) {
        this.inFlightCounter = inFlightCounter;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        this.inFlightCounter.incr();
        chain.doFilter(request, response);
        this.inFlightCounter.decr();
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 10;
    }

}
