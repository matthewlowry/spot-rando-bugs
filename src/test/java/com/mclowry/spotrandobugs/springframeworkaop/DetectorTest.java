package com.mclowry.spotrandobugs.springframeworkaop;

import com.mclowry.spotrandobugs.SpringFrameworkAopDetector;

import edu.umd.cs.findbugs.test.SpotBugsRule;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcherBuilder;

import org.junit.Rule;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static edu.umd.cs.findbugs.test.CountMatcher.containsExactly;
import static org.hamcrest.MatcherAssert.assertThat;


public class DetectorTest {

    @Rule
    public SpotBugsRule spotbugs = new SpotBugsRule();
    
    /**
     * Test that a class that should not trigger the detector, does, indeed, not trigger the detector.
     */
    @Test
    public void testGoodCase() {
        spotbugs.addAuxClasspathEntry(Paths.get("target/test-classes"));
        Path path = Paths.get("target/test-classes", "com/mclowry/spotrandobugs/springframeworkaop", "TrueNegative.class");
        assertThat(
            spotbugs.performAnalysis(path), 
            containsExactly(
                0,
                new BugInstanceMatcherBuilder().bugType(SpringFrameworkAopDetector.BUG_TYPE).build()));
    }

    /**
     * Test that a class that should trigger the detector, does, indeed, trigger the detector.
     */
    @Test
    public void testBadCase() {
        spotbugs.addAuxClasspathEntry(Paths.get("target/test-classes"));
        Path path = Paths.get("target/test-classes", "com/mclowry/spotrandobugs/springframeworkaop", "TruePositive.class");
        assertThat(
            spotbugs.performAnalysis(path), 
            containsExactly(
                5, 
                new BugInstanceMatcherBuilder().bugType(SpringFrameworkAopDetector.BUG_TYPE).build()));
    }

}
