package com.rocha.aws.app.route;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.rocha.aws.app.domain.Review;
import com.rocha.aws.app.repository.ReviewReactiveRepository;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReviewRouterIntegrationTest {

	@Autowired
	private WebTestClient webClient;

	@Autowired
	ReviewReactiveRepository reviewRepository;
	
	private final String URL_REVIEWS = "/v1/reviews";

	@BeforeEach
	void setUp() {

		var reviews = List.of(new Review("123", "movie1", "good movie", 5.0),
				              new Review("124", "movie1", "good movie", 4.5), 
				              new Review("125", "movie1", "good movie", 3.5),
				              new Review(null, "movie2", "Excelent movie", 5.0));
		
		reviewRepository.saveAll(reviews).blockLast();
	}
	
	@AfterEach
	void tearDown() {
		reviewRepository.deleteAll().block();
	}
	
	@Test
	void createAreview() {
		var review = new Review(null, "movie3", "Excelent movie", 4.5);
		
		webClient.post()
		.uri(URL_REVIEWS)
		.bodyValue(review)
		.exchange()
		.expectStatus()
		.isCreated()
		.expectBody(Review.class)
		.consumeWith(e -> {
			var reviewResponse = e.getResponseBody();
			assertNotNull(reviewResponse.getId());
			assertEquals("movie3", reviewResponse.getMovieInfoId());
		})
		;
		
	}
	
	@Test
	void getAllReviews() {
		webClient.get()
		.uri(URL_REVIEWS)
		.exchange()
		.expectStatus()
		.isOk()
		.expectBodyList(Review.class)
		.hasSize(4)
		;
		
	}
	
	@Test
	void upDateReviewById() {
		var reviewUpdate = new Review("123", "movie2", "excelent movie", 4.9);
		var reviewId = "123";
		webClient.put().uri(URL_REVIEWS+"/{id}",reviewId)
		.bodyValue(reviewUpdate)
		.exchange()
		.expectStatus()
		.isOk()
		.expectBody(Review.class)
		.consumeWith(e -> {
			var review = e.getResponseBody();
			assertEquals("123", review.getId());
			assertEquals("movie2", review.getMovieInfoId());
			assertEquals("excelent movie", review.getComment());
			assertEquals(4.9, review.getRating());
		})
		;
	}
	
	
	@Test
	void delteReviewById() {
		var reviewId = "123";
		webClient.delete().uri(URL_REVIEWS+"/{id}",reviewId)
		.exchange()
		.expectStatus()
		.is2xxSuccessful();
	}
	
	@Test
	void getReviewById() {
		var reviewId = "123";
		webClient.get().uri(URL_REVIEWS+"/{id}",reviewId)
		.exchange()
		.expectStatus()
		.isOk()
		.expectBody(Review.class)
		.consumeWith(e -> {
			var review = e.getResponseBody();
			assertEquals("123", review.getId());
			assertEquals("movie1", review.getMovieInfoId());
			assertEquals("good movie", review.getComment());
			assertEquals(5.0, review.getRating());
			
		});
	}
	
	@Test
	void getReviewByIdNotFound() {
		var reviewId = "1235789";
		webClient.get().uri(URL_REVIEWS+"/{id}",reviewId)
		.exchange()
		.expectStatus()
		.isNotFound();
	}
}
