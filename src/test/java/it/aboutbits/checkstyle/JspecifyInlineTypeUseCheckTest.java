package it.aboutbits.checkstyle;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JspecifyInlineTypeUseCheckTest extends CheckTestSupport {

    private static final String BASE = "/it/aboutbits/checkstyle/jspecifyInlineTypeUse/";

    @Test
    void inlineMethodReturnPasses() throws Exception {
        assertEquals(List.of(), runCheck(JspecifyInlineTypeUseCheck.class, BASE + "GoodInlineMethod.java"));
    }

    @Test
    void inlineFieldPasses() throws Exception {
        assertEquals(List.of(), runCheck(JspecifyInlineTypeUseCheck.class, BASE + "GoodInlineField.java"));
    }

    @Test
    void inlineParameterPasses() throws Exception {
        assertEquals(List.of(), runCheck(JspecifyInlineTypeUseCheck.class, BASE + "GoodInlineParameter.java"));
    }

    @Test
    void inlineLocalPasses() throws Exception {
        assertEquals(List.of(), runCheck(JspecifyInlineTypeUseCheck.class, BASE + "GoodInlineLocal.java"));
    }

    @Test
    void annotationOnLineAboveMethodFails() throws Exception {
        var v = runCheck(JspecifyInlineTypeUseCheck.class, BASE + "BadAboveMethod.java");
        assertEquals(1, v.size(), v.toString());
    }

    @Test
    void annotationOnLineAboveFieldFails() throws Exception {
        var v = runCheck(JspecifyInlineTypeUseCheck.class, BASE + "BadAboveField.java");
        assertEquals(1, v.size(), v.toString());
    }

    @Test
    void annotationOnLineAboveParameterFails() throws Exception {
        var v = runCheck(JspecifyInlineTypeUseCheck.class, BASE + "BadAboveParameter.java");
        assertEquals(1, v.size(), v.toString());
    }

    @Test
    void annotationOnLineAboveLocalFails() throws Exception {
        var v = runCheck(JspecifyInlineTypeUseCheck.class, BASE + "BadAboveLocal.java");
        assertEquals(1, v.size(), v.toString());
    }

    @Test
    void nonJspecifyNullableImportedFromElsewhereIsIgnored() throws Exception {
        assertEquals(
                List.of(),
                runCheck(JspecifyInlineTypeUseCheck.class, BASE + "NonJspecifyNullableAboveFieldIgnored.java")
        );
    }

    @Test
    void nonJspecifyFullyQualifiedNullableIsIgnored() throws Exception {
        assertEquals(
                List.of(),
                runCheck(JspecifyInlineTypeUseCheck.class, BASE + "NonJspecifyFqnNullableAboveFieldIgnored.java")
        );
    }

    @Test
    void fullyQualifiedJspecifyNullableAboveFieldFails() throws Exception {
        var v = runCheck(JspecifyInlineTypeUseCheck.class, BASE + "JspecifyFqnAboveFieldFails.java");
        assertEquals(1, v.size(), v.toString());
    }
}
