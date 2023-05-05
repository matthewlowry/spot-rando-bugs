package com.mclowry.spotrandobugs;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.PreorderDetector;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Method;
import org.objectweb.asm.Type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SpringFrameworkAopDetector extends PreorderDetector {

    public static final String BUG_TYPE = "SPRING_FRAMEWORK_AOP_NONPUBLIC";

    private static final Set<String> KNOWN_ANNOTATIONS_DESCRIPTORS = new HashSet<>(
        Arrays.asList(
            "Lorg/springframework/scheduling/annotation/Async;",
            "Lorg/springframework/scheduling/annotation/Scheduled;",
            "Lorg/springframework/transaction/annotation/Transactional;"));

    private static final Set<String> KNOWN_ANNOTATIONS_CLASSNAMES = new HashSet<>(
        Arrays.asList(
            "org.springframework.scheduling.annotation.Async",
            "org.springframework.scheduling.annotation.Scheduled",
            "org.springframework.transaction.annotation.Transactional"));

    private final BugReporter bugReporter;

    public SpringFrameworkAopDetector(BugReporter bugReporter) {
        this.bugReporter = bugReporter;
    }

    @Override
    public void visit(Method method) {

        // Straight-up ignore constructors the annotations we care about can't be applied to those.
        if (method.getName().equals("<init>")) {
            return;
        }

        // Public instance methods are fine.
        if (Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())) {
            return;
        }

        // A non-public instance method or a public static method eh?
        // Well check that none of the annotations we care about are being applied.
        for (AnnotationEntry annotation : method.getAnnotationEntries()) {

            // Is it directly applied?
            if (KNOWN_ANNOTATIONS_DESCRIPTORS.contains(annotation.getAnnotationType())) {
                bugReporter.reportBug(
                    new BugInstance(this, BUG_TYPE, NORMAL_PRIORITY)
                        .addClassAndMethod(this));
                return;
            }

            // Is it being applied as an inherited meta-annotation?
            // Use the ASM helper to map the type description to a "normal" class name and proceed reflectively.
            // Aside: could false positive if we see a chain that doesn't have @Inherited? Not sure.
            if (applyingAopAsMetaannotation(Type.getType(annotation.getAnnotationType()).getClassName())) {
                bugReporter.reportBug(
                    new BugInstance(this, BUG_TYPE, NORMAL_PRIORITY)
                        .addClassAndMethod(this));
                return;
            }
        }
    }

    // Examine an annotation to see if it is dragging in one of our Spring AOP annotations as a meta-annotation.
    // Code shamelessly stolen from Spring's AnnotationUtils.
    // XXX: Does SpotBugs/ BCEL / ASM offer anything convient? One would think so.

    private boolean applyingAopAsMetaannotation(String annotationClassName) {
        try {
            Class<?> asClass = Class.forName(annotationClassName);
            for (String aopAnnotationClassName : KNOWN_ANNOTATIONS_CLASSNAMES) {
                if (findAnnotation(asClass, Class.forName(aopAnnotationClassName))) {
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            bugReporter.reportMissingClass(e);
        } catch (Exception e) {
            // ... silently suppress? Should probably log this somehow?
        }
        return false;
    }

    private static boolean findAnnotation(Class<?> clazz, Class<?> annotationType) {
        return findAnnotation(clazz, annotationType, new HashSet<Annotation>());
    }

    private static boolean findAnnotation(Class<?> clazz, Class<?> annotationType, Set<Annotation> visited) {
        Annotation[] anns = clazz.getDeclaredAnnotations();
        for (Annotation ann : anns) {
            if (ann.annotationType() == annotationType) {
                return true;
            }
        }
        for (Annotation ann : anns) {
            if (ann.annotationType().getName().startsWith("java.lang.annotation") && visited.add(ann)) {
                if (findAnnotation(ann.annotationType(), annotationType, visited)) {
                    return true;
                }
            }
        }
        return false;
    }

}
