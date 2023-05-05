package com.mclowry.spotrandobugs.springdatajparepo;

import org.springframework.beans.factory.annotation.Autowired;

class TruePositive {
    
    @Autowired
    DummyRepo dummyRepo;
    
    @Autowired
    SubDummyRepo subDummyRepo;
    
    void callingGetOne() {
        dummyRepo.getOne("CORONAVIRUS");
    }
    
    void callingGetById() {
        subDummyRepo.getById("HANTAVIRUS");
    }
    
}
