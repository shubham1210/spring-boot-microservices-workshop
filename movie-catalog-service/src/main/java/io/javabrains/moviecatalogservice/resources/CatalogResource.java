package io.javabrains.moviecatalogservice.resources;

import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.UserRating;
import io.javabrains.moviecatalogservice.service.MovieInfo;
import io.javabrains.moviecatalogservice.service.UserRatingInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogResource {

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  WebClient.Builder webClientBuilder;

  @Autowired
  MovieInfo movieInfo;

  @Autowired
  UserRatingInfo userRatingInfo;

  @RequestMapping("/{userId}")
  // this is a way to handle at main api.
  //@HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
  public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

    UserRating userRating = userRatingInfo.getUserRating(userId);
    return userRating.getRatings().stream()
      .map(rating ->
        movieInfo.getCatalogItem(rating)
      )
      .collect(Collectors.toList());
  }

}
/*
Alternative WebClient way
Movie movie = webClientBuilder.build().get().uri("http://localhost:8082/movies/"+ rating.getMovieId())
.retrieve().bodyToMono(Movie.class).block();
*/
