package com.rocha.aws.app.error.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.rocha.aws.app.exception.ReviewDataException;
import com.rocha.aws.app.exception.ReviewNotFoundException;
import reactor.core.publisher.Mono;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final Map<Class<? extends Throwable>, ExceptionHandlingStrategy> exceptionHandlers = new HashMap<>();

    public GlobalExceptionHandler() {
        exceptionHandlers.put(ReviewDataException.class, new ReviewDataExceptionHandler());
        exceptionHandlers.put(ReviewNotFoundException.class, new ReviewNotFoundExceptionHandler());
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Exception message is {}", ex.getMessage());
        
        ExceptionHandlingStrategy strategy = exceptionHandlers.get(ex.getClass());
        if (strategy == null) {
            strategy = new DefaultExceptionHandler();
        }

        exchange.getResponse().setStatusCode(strategy.getHttpStatus());
        DataBuffer errorMessage = strategy.buildErrorMessage(exchange, ex);
        return exchange.getResponse().writeWith(Mono.just(errorMessage));
    }
}
