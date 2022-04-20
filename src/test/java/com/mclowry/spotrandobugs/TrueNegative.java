package com.mclowry.spotrandobugs;

import org.springframework.beans.factory.annotation.Autowired;

class TrueNegative {
    
    @Autowired
    DummyRepo dummyRepo;

    void method() {
        // Call some methods unrelated to the JPA repository interface.
        System.err.println("Hello SpotBugs!");
        // Call some methods of the JPA repository interface, but ones that aren't a problem.
        dummyRepo.count();
    }
    

}
