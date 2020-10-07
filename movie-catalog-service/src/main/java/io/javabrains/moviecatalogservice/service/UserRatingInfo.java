package io.javabrains.moviecatalogservice.service;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.javabrains.moviecatalogservice.models.Rating;
import io.javabrains.moviecatalogservice.models.UserRating;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserRatingInfo {

  private static final Log logger = LogFactory.getLog(UserRatingInfo.class);

  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private EurekaClient discoveryClient;



  public static String serviceUrl(String instanceName, EurekaClient client) {
    List<InstanceInfo> list = client.getInstancesByVipAddress(instanceName,false);
    if (list != null && list.size() > 0 ) {
      InstanceInfo applicationsInstance = list.get(0);
      String vip = applicationsInstance.getVIPAddress();
      String url = applicationsInstance.getHomePageUrl();
      return url.replace(applicationsInstance.getHostName(),vip);
    }
    return null;
  }
  @HystrixCommand(fallbackMethod = "getFallbackUserRating",
  commandProperties = {
    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "2000"),//how much time service should take max
    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",value = "5"), // how many request can be considered to make the desciosn
    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",value = "50"), // how much error % will drive the next decision
    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",value = "2000") // how long it will go back to the main service.

  })
  public UserRating getUserRating(@PathVariable("userId") String userId) {

    //for experiment purpose only.
    String ratingUrl = serviceUrl("ratings-data-service",discoveryClient);
    //getting url from client to check more things.
    logger.info(ratingUrl);
    //for experiment purpose only ends .

    return restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
  }

  public UserRating getFallbackUserRating(@PathVariable("userId") String userId) {
    UserRating userRating = new UserRating();
    userRating.setUserId(userId);
    userRating.setRatings(Arrays.asList(new Rating("0", 0)));
    return userRating;
  }
}
