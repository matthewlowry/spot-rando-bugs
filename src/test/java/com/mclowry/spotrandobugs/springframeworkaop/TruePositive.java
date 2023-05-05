package com.mclowry.spotrandobugs.springframeworkaop;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

public class TruePositive {

    @Async
    protected void someMethodA(String someArg) {
        // ... 
    }

    @Transactional
    protected void someMethodT(String someArg) {
        // ... 
    }

    @Scheduled
    protected void someMethodS(String someArg) {
        // ... 
    }

    @MetaTransactional
    private void someMethodMT(String someArg) {
        // ... 
    }

    @Async
    public static void someStaticMethodA(String someArg) {
        // ... 
    }

}
