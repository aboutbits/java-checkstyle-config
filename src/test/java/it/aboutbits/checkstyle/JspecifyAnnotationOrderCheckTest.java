package it.aboutbits.checkstyle;

import org.junit.jupiter.api.Test;

import java.util.List;

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
    void jspecifyAnnotationFollowedByOtherFails() throws Exception {
        var v = runCheck(JspecifyAnnotationOrderCheck.class, BASE + "BadInMiddle.java");
        assertEquals(1, v.size());
        assertTrue(v.get(0).contains("NullMarked"), v.toString());
    }

    @Test
    void jspecifyAnnotationAsFirstFails() throws Exception {
        var v = runCheck(JspecifyAnnotationOrderCheck.class, BASE + "BadFirst.java");
        assertEquals(1, v.size());
    }

    @Test
    void noJspecifyAnnotationDoesNotFire() throws Exception {
        var v = runCheck(JspecifyAnnotationOrderCheck.class, BASE + "NoJspecify.java");
        assertEquals(List.of(), v);
    }
}
