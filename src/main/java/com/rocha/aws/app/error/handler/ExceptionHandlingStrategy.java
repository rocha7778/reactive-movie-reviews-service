package com.rocha.aws.app.error.handler;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

public interface ExceptionHandlingStrategy {
	 HttpStatus getHttpStatus();
	 DataBuffer buildErrorMessage(ServerWebExchange exchange, Throwable ex);
}
