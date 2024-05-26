package com.rocha.aws.app.route;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.rocha.aws.app.domain.Review;
import com.rocha.aws.app.repository.ReviewReactiveRepository;

import reactor.core.publisher.Mono;

@AutoConfigureWebTestClient
@ActiveProfiles("test")
@SpringBootTest
public class ReviewRouterUnitTest {

	@Autowired
	private WebTestClient webClient;

	@MockBean
	ReviewReactiveRepository reviewRepository;
	
	private final String URL_REVIEWS = "/v1/reviews";

	
	@Test
	void createAreview() {
		var review = new Review(null, null, "Excelent movie", -3.0);
		
		webClient.post()
		.uri(URL_REVIEWS)
		.bodyValue(review)
		.exchange()
		.expectStatus()
		.isBadRequest()
		.expectBody(String.class)
		.consumeWith(e -> {
			var errorMessage = e.getResponseBody();
			var messageExpected = "rating.negative : rating is negative and please pass a non-negative value,review.moveInfoId : must be present";
			assertEquals(messageExpected, errorMessage);
		})
		
		;
		
	}
	
	
	@Test
	void getReviewById() {
		var reviewId = "123";
		Review reviewMock = new Review("123", "movie1", "good movie", 5.0);
		when(reviewRepository.findById(reviewId)).thenReturn(Mono.just(reviewMock));
		
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
		var reviewId = "123";
		when(reviewRepository.findById(reviewId)).thenReturn(Mono.empty());
		webClient.get().uri(URL_REVIEWS+"/{id}",reviewId)
		.exchange()
		.expectStatus()
		.isNotFound();
	}
	
	
	@Test
	void delteReviewById() {
		var reviewId = "123";
		when(reviewRepository.deleteById(reviewId)).thenReturn(Mono.empty());
		webClient.delete().uri(URL_REVIEWS+"/{id}",reviewId)
		.exchange()
		.expectStatus()
		.is2xxSuccessful();
	}
	
	
}
