# Spring  boot + discovery server(Eureka) + Circuit Breaker pattern -Hystrix + Hystrix dashboard

## Service discovery 
1. Way to discover a service that is too many in number. 
2. Client side discovery is when a client talks to the discovery server and then hit the main service. 
3. Server side discovery talks to the discovery server(api gateway) then api gateway calls the main service. 
4. Spring cloud uses client side service discovery. 
5. Spring cloud can be used with Eureka , for discovery.some examples are Eureka, Ribbon, Hysterix, Zuul
6. Spring cloud based on from netflix. 

## Eureka 
```python 
1.every Eureka server is also a client so we can configure it don't regeier yourself.
2. it also does load balance(client sides),but not a good idea to do the client side load balance. 
3. discovery client is the class that helps to see all the services attached to one title and get the list of services. 
In case you want to do it manually. But rest template can handle it as abstraction so lets omit it.
4.  Heartbeat sent by client to make sure they are up.
5. if discovery is down, then the client uses cache.
6. discovery service config.
7. server.port=8761 , eureka.client.register-with-eureka=false, eureka.client.fetch-registry=false
8. server needs this annotations @EnableEurekaServer
9. client needs this annotations @EnableEurekaClient
```

# Handling Traffic(Circuit Breaker-Hystrix) :
1. Use timeout if your microservices are dependent on slow service , so that tomcat doesn't load up as he is keeping multiple threads waiting for the slow thread to complete.
2. We can set a timeout using resttemplate.
3. But it is always going to w8 for that time. So what we can do better to circuit break(it is like the MCB on electricity board break when flow is high ), just split the traffic with other instances of the mc.
so detect what is wrong and what is slow
4. take some steps to divide the traffic to other instances. 
5.So we need to define some params like , how many requests you want to watch and after for the failure , and what should be the time out and for how long mc should go to sleep then again try that.
And when the circuit break do the fallback ,
6. you can throw an error , but try to send an error response rather than error.
7. you can cache the response and send back that.

# Important Notes
### If we are going to call the same method in a class that has hystrix command annotation it will not call the fallback , as we are calling inside the same proxy class.
### To make this working we need to pull that hystrix annotation to another bean so that hystrix gets to know why it is not working.
### We can configure thread pools(### BulkHead) for specific services to free up the server and also won't disturb the other server. Need to configure buckets so that each microservice can only be part of it.

# Userful Links for setting up at local 
1. ### http://localhost:8081/hystrix for dashboard. (http://localhost:8081/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A8081%2Factuator%2Fhystrix.stream)
2. ### http://localhost:8761/ - discovery server
3. ### http://192.168.29.140:8082/movies/1  - movie API
4. ### http://localhost:8083/ratingsdata/user/1  - user API

#Files to Look up
1. MovieCatalogServiceApplication
2. UserRatingInfo

#local Links
http://localhost:8082/movies/1
http://localhost:8081/catalog/1
http://localhost:8083/ratingsdata/movies/1
http://localhost:8761/
