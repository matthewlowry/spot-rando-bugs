package com.mclowry.spotrandobugs.springdatajparepo;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * An extension of an extension of the Spring Data JPA repository inteface.
 */
public interface SubDummyRepo extends JpaRepository<String, String> {

}
