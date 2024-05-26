package com.rocha.aws.app.route;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.rocha.aws.app.handler.ReviewHandler;

@Configuration
public class ReviewRouter {
	
	@Autowired
	private ReviewHandler hanlder;
	
	@Bean
	RouterFunction<ServerResponse> reviewRoute(){
		
		return RouterFunctions.route()
				.POST("/v1/reviews", hanlder::addReview)
				.PUT("/v1/reviews/{id}", hanlder::updateReview)
				.DELETE("/v1/reviews/{id}", hanlder::delteReviewById)
				.GET("/v1/reviews/{id}", hanlder::getReviewById)
				.GET("/v1/reviews",hanlder::getReviews)
				.build();
	}
	

}
