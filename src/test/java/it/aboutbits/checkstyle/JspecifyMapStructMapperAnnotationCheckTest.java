package it.aboutbits.checkstyle;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NullMarked
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
        assertTrue(v.getFirst().contains("BadOrderSwapped"), v.toString());
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

    @Test
    void customMapperAnnotationNameIsHonored() throws Exception {
        var properties = Map.of("mapperAnnotationName", "MyMapper");

        var ok = runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "CustomMapperGood.java", properties);
        assertEquals(List.of(), ok);

        var bad = runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "CustomMapperBad.java", properties);
        assertEquals(1, bad.size());
        assertTrue(bad.getFirst().contains("CustomMapperBad"), bad.toString());
    }

    @Test
    void customAnnotateWithAnnotationNameIsHonored() throws Exception {
        var properties = Map.of("annotateWithAnnotationName", "MyAnnotateWith");

        var ok = runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "CustomAnnotateWithGood.java", properties);
        assertEquals(List.of(), ok);

        var bad = runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "GoodMapper.java", properties);
        assertEquals(1, bad.size(), bad.toString());
    }

    @Test
    void customNullUnmarkedAnnotationNameIsHonored() throws Exception {
        var properties = Map.of("nullUnmarkedAnnotationName", "MyUnmarked");

        var ok = runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "CustomNullUnmarkedGood.java", properties);
        assertEquals(List.of(), ok);

        var bad = runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "GoodMapper.java", properties);
        assertEquals(1, bad.size(), bad.toString());
    }

    @Test
    void wellOrderedAbstractClassMapperPasses() throws Exception {
        assertEquals(
                List.of(),
                runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "GoodAbstractMapper.java")
        );
    }

    @Test
    void abstractClassMapperWithSwappedOrderFails() throws Exception {
        var v = runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "BadAbstractMapperOrderSwapped.java");
        assertEquals(1, v.size());
        assertTrue(v.getFirst().contains("BadAbstractMapperOrderSwapped"), v.toString());
    }

    @Test
    void nonAbstractClassWithMapperAnnotationIsIgnored() throws Exception {
        assertEquals(
                List.of(),
                runCheck(JspecifyMapStructMapperAnnotationCheck.class, BASE + "NonAbstractClassWithMapperAnnotationIgnored.java")
        );
    }
}
