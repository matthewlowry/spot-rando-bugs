package com.mclowry.spotrandobugs;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;

import edu.umd.cs.findbugs.test.SpotBugsRule;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcherBuilder;

import static edu.umd.cs.findbugs.test.CountMatcher.containsExactly;
import static org.hamcrest.MatcherAssert.assertThat;


public class SpringDataJpaRepoDetectorTest {

    @Rule
    public SpotBugsRule spotbugs = new SpotBugsRule();
    
    @Test
    public void testGoodCase() {
        spotbugs.addAuxClasspathEntry(Paths.get("target/test-classes"));
        Path path = Paths.get("target/test-classes", "com/mclowry/spotrandobugs", "TrueNegative.class");
        assertThat(
            spotbugs.performAnalysis(path), 
            containsExactly(
                0,
                new BugInstanceMatcherBuilder().bugType(SpringDataJpaRepoDetector.BUG_TYPE).build()));
    }

    @Test
    public void testBadCase() {
        spotbugs.addAuxClasspathEntry(Paths.get("target/test-classes"));
        Path path = Paths.get("target/test-classes", "com/mclowry/spotrandobugs", "TruePositive.class");
        assertThat(
            spotbugs.performAnalysis(path), 
            containsExactly(
                2, 
                new BugInstanceMatcherBuilder().bugType(SpringDataJpaRepoDetector.BUG_TYPE).build()));
    }
}
