# Spot Rando Bugs

A Spotbugs plugin to provide detectors for things that really grind Matt's gears.

## Bug Patterns

* `SPRING_DATA_JPA_GET`
   The `getById` method of Spring Data JPA's `JpaRepository` interface, 
   and its deprecated equivalent `getOne`, can be troublesome.
   The behaviour of these methods can vary depending on the underlying JPA provider.
   If passed an entity identifier that does not exist, these methods are liable to return a reference
   to a proxy that throws EntityNotFoundException if accessed, which is typically not what developers
   expect. The alternative findById method returns an Optional to explicitly signal whether an entity
   with the given ID exists, which leads to simpler, cleaner, and more explicit code.

## Notes

* Assumes a fairly recent Spotbugs. Currently binding against Spotbugs 4.6.0. Will probably work
  as intended with Spotbugs 4.5.x. May not work with earlier versions of Spotbugs.
* I have no idea how to pattern ranks work. Not sure if anything I've done to try and customise
  the rank used for patterns in this plugin will work. I _think_ spotbugs is just using a default
  rank of 14 for everything?