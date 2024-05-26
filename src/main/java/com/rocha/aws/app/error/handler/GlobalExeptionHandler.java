package com.rocha.aws.app.error.handler;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.rocha.aws.app.exception.ReviewDataException;
import com.rocha.aws.app.exception.ReviewNotFoundException;

import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalExeptionHandler {

	/*
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		// TODO Auto-generated method stub
		log.error("Exception message is {}", ex.getMessage());
		
		DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
		var errorMessage = dataBufferFactory.wrap(ex.getMessage().getBytes());
		
		if(ex instanceof ReviewDataException) {
			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			return exchange.getResponse().writeWith(Mono.just(errorMessage));
		}
		
		if(ex instanceof ReviewNotFoundException) {
			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			return exchange.getResponse().writeWith(Mono.just(errorMessage));
		}
		exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
		return exchange.getResponse().writeWith(Mono.just(errorMessage));
	}
	*/

}
