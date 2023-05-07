# Spot Rando Bugs

A Spotbugs plugin to provide detectors for things that really grind Matt's gears.

## Bug Patterns

`SPRING_DATA_JPA_GET`

The `getById` method of Spring Data JPA's `JpaRepository` interface, and its deprecated equivalent
`getOne`, can be troublesome. The behaviour of these methods can vary depending on the underlying 
JPA provider. If passed an entity identifier that does not exist, these methods are liable to return 
a reference to a proxy that throws `EntityNotFoundException` if accessed, which is typically not 
what developers expect. The alternative `findById` method returns an `Optional` to explicitly signal
whether an entity with the given ID exists, which leads to simpler, cleaner, and more explicit code.

`SPRING_FRAMEWORK_AOP_NONPUBLIC`

AOP annotations rely on generating proxies for the annotated class, injecting those proxies rather 
than the "naked" bean, and achieving the specified behavior via method interception implemented in 
the proxy. Although it is technically feasible to intercept calls to non-public methods when proxies 
are being generated using CGLIB, Spring as a matter of design chooses not to for annotations like 
`@Transactional`; placing AOP annotations like this on non-public or static methods silently fails.

This pattern specifically looks for any of the following being applied to a method that is not a 
public instance method:
* `@org.springframework.scheduling.annotation.Async`
* `@org.springframework.scheduling.annotation.Scheduled` 
* `@org.springframework.transaction.annotation.Transactional`

Strictly speaking `@Scheduled` is not an AOP-based annotation - it is implemented by reflective 
scanning and direct invocation. However in my experience it is best to enforce uniformity in
treatment across these annotations and 

## Notes

* Assumes a fairly recent Spotbugs. Currently binding against Spotbugs 4.7.3. Will probably work
  as intended with older versions of Spotbugs down to 4.5.x. May not work with earlier versions of 
  Spotbugs.
* I have no idea how to pattern ranks work. Not sure if anything I've done to try and customise
  the rank used for patterns in this plugin will work. I _think_ spotbugs is just using a default
  rank of 14 for everything?
