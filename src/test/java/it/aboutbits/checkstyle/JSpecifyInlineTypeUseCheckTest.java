package it.aboutbits.checkstyle;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@NullMarked
class JSpecifyInlineTypeUseCheckTest extends CheckTestSupport {

    private static final String BASE = "/it/aboutbits/checkstyle/jspecifyInlineTypeUse/";

    @Test
    void inlineMethodReturnPasses() throws Exception {
        assertEquals(List.of(), runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "GoodInlineMethod.java"));
    }

    @Test
    void inlineFieldPasses() throws Exception {
        assertEquals(List.of(), runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "GoodInlineField.java"));
    }

    @Test
    void inlineParameterPasses() throws Exception {
        assertEquals(List.of(), runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "GoodInlineParameter.java"));
    }

    @Test
    void inlineLocalPasses() throws Exception {
        assertEquals(List.of(), runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "GoodInlineLocal.java"));
    }

    @Test
    void annotationOnLineAboveMethodFails() throws Exception {
        var v = runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "BadAboveMethod.java");
        assertEquals(1, v.size(), v.toString());
    }

    @Test
    void annotationOnLineAboveFieldFails() throws Exception {
        var v = runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "BadAboveField.java");
        assertEquals(1, v.size(), v.toString());
    }

    @Test
    void annotationOnLineAboveParameterFails() throws Exception {
        var v = runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "BadAboveParameter.java");
        assertEquals(1, v.size(), v.toString());
    }

    @Test
    void annotationOnLineAboveLocalFails() throws Exception {
        var v = runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "BadAboveLocal.java");
        assertEquals(1, v.size(), v.toString());
    }

    @Test
    void nonJspecifyNullableImportedFromElsewhereIsIgnored() throws Exception {
        assertEquals(
                List.of(),
                runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "NonJSpecifyNullableAboveFieldIgnored.java")
        );
    }

    @Test
    void nonJspecifyFullyQualifiedNullableIsIgnored() throws Exception {
        assertEquals(
                List.of(),
                runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "NonJSpecifyFqnNullableAboveFieldIgnored.java")
        );
    }

    @Test
    void fullyQualifiedJspecifyNullableAboveFieldFails() throws Exception {
        var v = runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "JSpecifyFqnAboveFieldFails.java");
        assertEquals(1, v.size(), v.toString());
    }
}
