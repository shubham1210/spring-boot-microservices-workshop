package io.javabrains.moviecatalogservice.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class MovieInfo {

  @Autowired
  private RestTemplate restTemplate;

  @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
  public CatalogItem getCatalogItem(Rating rating) {
    Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
    return new CatalogItem(movie.getName(), movie.getDescription(), rating.getRating());
  }

  /*
    so just to test it just drop the movie info service then this you will see it will call this method.
   */
  public CatalogItem getFallbackCatalogItem(Rating rating) {
    return new CatalogItem("fallback Movie", "desc", rating.getRating());
  }
}
