package com.rocha.aws.app.error.handler;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

public class DefaultExceptionHandler implements ExceptionHandlingStrategy{

	@Override
	public HttpStatus getHttpStatus() {
	  return HttpStatus.INTERNAL_SERVER_ERROR;
	}

	@Override
	public DataBuffer buildErrorMessage(ServerWebExchange exchange, Throwable ex) {
		 DataBufferFactory dataBufferFactory = exchange.getResponse().bufferFactory();
	     return dataBufferFactory.wrap(ex.getMessage().getBytes());
	}

}
