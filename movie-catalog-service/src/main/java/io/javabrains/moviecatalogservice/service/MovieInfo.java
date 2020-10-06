package io.javabrains.moviecatalogservice.service;

import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.javabrains.moviecatalogservice.models.CatalogItem;
import io.javabrains.moviecatalogservice.models.Movie;
import io.javabrains.moviecatalogservice.models.Rating;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfo {
  Log logger = LogFactory.getLog(MovieInfo.class);

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  EurekaClient dicEurekaClient;


  @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
  public CatalogItem getCatalogItem(Rating rating) {

    //for experiment purpose only.
    //getting url from client to check more things.
    String movieInfoUrl = UserRatingInfo.serviceUrl("movie-info-service",dicEurekaClient);
    logger.info(movieInfoUrl);
    //for experiment purpose only ends .

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
