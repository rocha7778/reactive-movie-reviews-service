package com.rocha.aws.app.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Review {
	
	@Id
	private String id;
	@NotNull(message = "review.movieInfoId : must be present")
	private String movieInfoId;
	private String comment;
	@Min(value = 0L, message = "rating.negative : rating is negative and please pass a non-negative value")
	private Double rating;
	
	

}
