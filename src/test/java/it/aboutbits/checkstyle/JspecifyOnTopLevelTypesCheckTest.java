package it.aboutbits.checkstyle;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JspecifyOnTopLevelTypesCheckTest extends CheckTestSupport {

    private static final String BASE = "/it/aboutbits/checkstyle/jspecifyOnTopLevelTypes/";

    @Test
    void classMarkedWithNullMarkedPasses() throws Exception {
        var v = runCheck(JspecifyOnTopLevelTypesCheck.class, BASE + "GoodMarked.java");
        assertEquals(List.of(), v);
    }

    @Test
    void classMarkedWithNullUnmarkedPasses() throws Exception {
        var v = runCheck(JspecifyOnTopLevelTypesCheck.class, BASE + "GoodUnmarked.java");
        assertEquals(List.of(), v);
    }

    @Test
    void fullyQualifiedJspecifyAnnotationPasses() throws Exception {
        var v = runCheck(JspecifyOnTopLevelTypesCheck.class, BASE + "GoodFullyQualified.java");
        assertEquals(List.of(), v);
    }

    @Test
    void classWithoutJspecifyAnnotationFails() throws Exception {
        var v = runCheck(JspecifyOnTopLevelTypesCheck.class, BASE + "BadMissing.java");
        assertEquals(1, v.size());
        assertTrue(v.get(0).contains("BadMissing"), v.toString());
    }

    @Test
    void annotationDeclarationIsExempt() throws Exception {
        var v = runCheck(JspecifyOnTopLevelTypesCheck.class, BASE + "AnnotationDefExempt.java");
        assertEquals(List.of(), v);
    }

    @Test
    void nestedClassIsNotChecked() throws Exception {
        var v = runCheck(JspecifyOnTopLevelTypesCheck.class, BASE + "NestedClassOnly.java");
        assertEquals(List.of(), v);
    }

    @Test
    void recordWithoutJspecifyFails() throws Exception {
        var v = runCheck(JspecifyOnTopLevelTypesCheck.class, BASE + "BadRecord.java");
        assertEquals(1, v.size());
        assertTrue(v.get(0).contains("BadRecord"), v.toString());
    }
}
