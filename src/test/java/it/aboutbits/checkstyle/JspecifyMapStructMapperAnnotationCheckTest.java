package it.aboutbits.checkstyle;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JspecifyMapStructMapperAnnotationCheckTest extends CheckTestSupport {

    private static final String BASE = "/it/aboutbits/checkstyle/jspecifyMapStructMapperAnnotation/";

    @Test
    void wellOrderedMapperPasses() throws Exception {
        assertEquals(List.of(), runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "GoodMapper.java"));
    }

    @Test
    void mapperWithNamedValueArgumentPasses() throws Exception {
        assertEquals(
                List.of(),
                runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "GoodMapperNamedValue.java")
        );
    }

    @Test
    void swappedOrderFails() throws Exception {
        var v = runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "BadOrderSwapped.java");
        assertEquals(1, v.size());
        assertTrue(v.get(0).contains("BadOrderSwapped"), v.toString());
    }

    @Test
    void missingAnnotateWithFails() throws Exception {
        var v = runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "BadMissingAnnotateWith.java");
        assertEquals(1, v.size());
    }

    @Test
    void annotateWithWrongClassLiteralFails() throws Exception {
        var v = runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "BadWrongClassLiteral.java");
        assertEquals(1, v.size());
    }

    @Test
    void interfaceWithoutMapperIsIgnored() throws Exception {
        assertEquals(
                List.of(),
                runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "NonMapperInterfaceIgnored.java")
        );
    }
}
