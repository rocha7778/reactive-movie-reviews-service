package com.rocha.aws.app.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.rocha.aws.app.domain.Review;

import reactor.core.publisher.Flux;

public interface ReviewReactiveRepository extends ReactiveMongoRepository<Review, String>{
	
	public Flux<Review> findReviewsByMovieInfoId(String movieInfoId);

}
