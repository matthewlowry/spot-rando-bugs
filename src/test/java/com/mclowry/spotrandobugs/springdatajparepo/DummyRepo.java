package com.mclowry.spotrandobugs.springdatajparepo;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An extension of the Spring Data JPA repository inteface.
 */
public interface DummyRepo extends JpaRepository<String, String> {

}
