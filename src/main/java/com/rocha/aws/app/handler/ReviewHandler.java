package com.rocha.aws.app.handler;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.rocha.aws.app.domain.Review;
import com.rocha.aws.app.exception.ReviewDataException;
import com.rocha.aws.app.repository.ReviewReactiveRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ReviewHandler {

	@Autowired
	private ReviewReactiveRepository reviewRepository;

	@Autowired
	private Validator validator;

	public Mono<ServerResponse> addReview(ServerRequest request) {

		Mono<Review> reviewRq = request.bodyToMono(Review.class);
		return reviewRq
				.doOnNext(this::validate)
				.flatMap(reviewRepository::save)
				.flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
	}

	private void validate(Review review) {
		var constrainst = validator.validate(review);
		log.info("contrainstViolations : {} ", constrainst);

		if (constrainst.size() > 0) {
			var errorMessage = constrainst.stream()
					.map(ConstraintViolation::getMessage)
					.sorted()
					.collect(Collectors.joining(","));
			throw new ReviewDataException(errorMessage);
		}

	}

	public Mono<ServerResponse> getReviews(ServerRequest request) {
		var movieId = request.queryParam("movieId");
		if(movieId.isPresent()) {
			return buildServerResponse(reviewRepository.findReviewsByMovieInfoId(movieId.get()));
		}else {
			return buildServerResponse(reviewRepository.findAll());
		}
		
	}
	
	private Mono<ServerResponse> buildServerResponse(Flux<Review> reviews){
		return ServerResponse.status(HttpStatus.OK).body(reviews, Review.class);
	}

	public Mono<ServerResponse> updateReview(ServerRequest request) {
		return request.bodyToMono(Review.class).flatMap(reviewRq -> {
			return reviewRepository.findById(request.pathVariable("id")).flatMap(reviewdb -> {
				reviewdb.setComment(reviewRq.getComment());
				reviewdb.setMovieInfoId(reviewRq.getMovieInfoId());
				reviewdb.setRating(reviewRq.getRating());
				return reviewRepository.save(reviewdb);
			});
		}).flatMap(ServerResponse.status(HttpStatus.OK)::bodyValue).switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> delteReviewById(ServerRequest request) {
		return reviewRepository.deleteById(request.pathVariable("id"))
				.then(ServerResponse.status(HttpStatus.NO_CONTENT).build());
	}

	public Mono<ServerResponse> getReviewById(ServerRequest request) {

		return reviewRepository.findById(request.pathVariable("id"))
				.flatMap(ServerResponse.status(HttpStatus.OK)::bodyValue)
				.switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build());
	}
}
