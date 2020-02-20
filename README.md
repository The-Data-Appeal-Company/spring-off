# spring-off

[ ![Download](https://api.bintray.com/packages/datappeal/datappeal/spring-off/images/download.svg) ](https://bintray.com/datappeal/datappeal/spring-off/_latestVersion)
![Build](https://github.com/The-Data-Appeal-Company/spring-off/workflows/Build/badge.svg)
[![codecov](https://codecov.io/gh/The-Data-Appeal-Company/spring-off/branch/master/graph/badge.svg)](https://codecov.io/gh/The-Data-Appeal-Company/spring-off)

### Graceful shutdown for spring boot in running kubernetes

spring-off allow graceful shutdown of you spring boot based api by taking advatage 
of the kubernetes readyness probe it allow safe instance terminations by stopping 
the incoming traffic and waiting for all in-flight requests to be completed.


## How does it work 

spring-off intercepts SIGTERM signal sent by kubernetes and it switches off the readiness probe for the instance, 
by doing so kubernetes will stop forwarding traffic to the pod, then it will wait for all inflight requests to be 
completed before shutting down the application instance.  

## Integration

### Spring 

In order to activate the graceful shutdown you must add the *@GracefulShutdown* and use the *GracefulShutdownApp* class to start you application 

```java
    @GracefulShutdown
    @SpringBootApplication
    public static class MySpringApplication {
        public static void main(String[] args) {
            GracefulShutdownApp.run(MySpringApplication.class, args);
        }
    }
``` 

### Kubernetes deployment 

You must switch your readiness probe to the spring-off managed endpoint `/ready`


### Install

```groovy
repositories {
    maven {
        url  "https://dl.bintray.com/datappeal/datappeal" 
    }
} 

implementation 'io.datappeal.spring:spring-off:<version>'
```


