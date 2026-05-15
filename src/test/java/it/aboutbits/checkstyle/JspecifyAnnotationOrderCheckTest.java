package it.aboutbits.checkstyle;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JspecifyAnnotationOrderCheckTest extends CheckTestSupport {

    private static final String BASE = "/it/aboutbits/checkstyle/jspecifyAnnotationOrder/";

    @Test
    void jspecifyAnnotationAsLastPasses() throws Exception {
        var v = runCheck(JspecifyAnnotationOrderCheck.class, BASE + "GoodLast.java");
        assertEquals(List.of(), v);
    }

    @Test
    void jspecifyAnnotationFollowedByOtherFailsAndHighlightsCloseAnnotation() throws Exception {
        var v = runCheck(JspecifyAnnotationOrderCheck.class, BASE + "BadInMiddle.java");
        assertEquals(1, v.size());
        assertTrue(v.get(0).contains("NullMarked"), v.toString());
        assertTrue(v.get(0).startsWith("6:"), "violation should be reported on the @NullMarked line: " + v);
    }

    @Test
    void jspecifyAnnotationAsFirstFailsAndHighlightsCloseAnnotation() throws Exception {
        var v = runCheck(JspecifyAnnotationOrderCheck.class, BASE + "BadFirst.java");
        assertEquals(1, v.size());
        assertTrue(v.get(0).startsWith("5:"), "violation should be reported on the @NullMarked line: " + v);
    }

    @Test
    void noJspecifyAnnotationDoesNotFire() throws Exception {
        var v = runCheck(JspecifyAnnotationOrderCheck.class, BASE + "NoJspecify.java");
        assertEquals(List.of(), v);
    }

    @Test
    void customCloseAnnotationsArePicked() throws Exception {
        var properties = Map.of("closeAnnotations", "Custom,AnotherClose");

        var ok = runCheck(JspecifyAnnotationOrderCheck.class, BASE + "CustomCloseLast.java", properties);
        assertEquals(List.of(), ok);

        var bad = runCheck(JspecifyAnnotationOrderCheck.class, BASE + "CustomCloseInMiddle.java", properties);
        assertEquals(1, bad.size());
        assertTrue(bad.get(0).contains("Custom"), bad.toString());
    }

    @Test
    void closeAnnotationsAreTrimmed() throws Exception {
        var v = runCheck(
                JspecifyAnnotationOrderCheck.class,
                BASE + "GoodLast.java",
                Map.of("closeAnnotations", "  NullMarked , NullUnmarked  ")
        );
        assertEquals(List.of(), v);
    }
}
