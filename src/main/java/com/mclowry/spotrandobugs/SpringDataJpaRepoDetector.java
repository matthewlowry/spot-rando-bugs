/*
Copyright (c) 2022 Matthew Clifton Lowry

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.mclowry.spotrandobugs;

import org.apache.bcel.Const;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.Hierarchy;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;

/**
 * Detects invocation of the Spring Data JpaRepository {@code getOne} and {@code getById} methods. 
 * 
 * @author matthewlowry
 */
public class SpringDataJpaRepoDetector extends OpcodeStackDetector {

    public static final String BUG_TYPE = "SPRING_DATA_JPA_GET";

    private final BugReporter bugReporter;

    public SpringDataJpaRepoDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen == Const.INVOKEINTERFACE && isRepo(getDottedClassConstantOperand())) {
            String targetMethodName = getNameConstantOperand();
            if (targetMethodName.equals("getOne") || targetMethodName.equals("getById")) {
                bugReporter.reportBug(
                    new BugInstance(this, BUG_TYPE, NORMAL_PRIORITY)
                        .addClassAndMethod(this)
                        .addSourceLine(this, getPC()));
            }
        }
    }

    private boolean isRepo(String clazz) {
        try {
            return Hierarchy.isSubtype(clazz, "org.springframework.data.jpa.repository.JpaRepository");
        } catch (ClassNotFoundException e) {
            bugReporter.reportMissingClass(e);
            return false;
        }
    }

}
