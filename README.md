# spring-off

[![Download](https://api.bintray.com/packages/datappeal/datappeal/spring-off/images/download.svg) ](https://bintray.com/datappeal/datappeal/spring-off/_latestVersion)
![Build](https://github.com/The-Data-Appeal-Company/spring-off/workflows/Build/badge.svg)
[![codecov](https://codecov.io/gh/The-Data-Appeal-Company/spring-off/branch/master/graph/badge.svg)](https://codecov.io/gh/The-Data-Appeal-Company/spring-off)
[![license](https://img.shields.io/github/license/The-Data-Appeal-Company/spring-off.svg)](LICENSE.txt)

We hope you and your offspring will gracefully turn off spring using spring-off :)

### Graceful shutdown for spring boot in running kubernetes

spring-off allow graceful shutdown of you spring boot based api by handling correctly 
the SIGTERM signal sent by kubernetes in event of pod termination

## How does it work 

spring-off intercepts SIGTERM signal sent by kubernetes and then it waits for a fixed amount
of time in order to allow the propagation of the iptable configuration that detaches the current
pod from the service, in the meanwhile kubernetes may still forward requests to the pod so 
spring-off take care of waiting for all inflight request to be completed before closing the 
spring context.

Other libraries also switch off readiness probe before beginning the shutdown procedure, but
actually this is not useful in order to improve traffic routing since the pod is already
detached from the service when its state is changed to terminating.

Reference: 

[kubernetes.io - termination-of-pods](https://kubernetes.io/docs/concepts/workloads/pods/pod/#termination-of-pods)

[graceful shutdown in kubernetes](https://blog.laputa.io/graceful-shutdown-in-kubernetes-85f1c8d586da )

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

### Install

```groovy
repositories {
    maven {
        url  "https://dl.bintray.com/datappeal/datappeal" 
    }
} 

implementation 'io.datappeal.spring:spring-off:<version>'
```


