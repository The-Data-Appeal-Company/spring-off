# spring-off

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
            GracefulShutdownApp.run(TestSpringApplication.class, args);
        }
    }
``` 

### Kubernetes deployment 

You must switch your readiness probe to the spring-off managed endpoint `/ready`





