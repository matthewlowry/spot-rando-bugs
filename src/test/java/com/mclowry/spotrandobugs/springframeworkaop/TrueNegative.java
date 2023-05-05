package com.mclowry.spotrandobugs.springframeworkaop;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

public class TrueNegative {

    @Async
    public void someMethodA(String someArg) {
        // ... 
    }

    @Transactional
    public void someMethodT(String someArg) {
        // ... 
    }

    @Scheduled
    public void someMethodS(String someArg) {
        // ... 
    }

    @MetaTransactional
    public void someMethodMT(String someArg) {
        // ... 
    }

}
