# Spot Rando Bugs

A Spotbugs plugin to provide detectors for things that really grind Matt's gears.

## Bug Patterns

* `SPRING_DATA_JPA_GET`
   Both the getById method and its deprecated equivalent getOne can be troublesome.
   The behaviour of these methods can vary depending on the underlying JPA provider.
   If passed an entity identifier that does not exist, these methods are liable to return a reference
   to a proxy that throws EntityNotFoundException if accessed, which is typically not what developers
   expect. The alternative findById method returns an Optional to explicitly signal whether an entity
   with the given ID exists, which leads to simpler, cleaner, and more explicit code.
