package it.aboutbits.checkstyle;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class JSpecifyInlineTypeUseCheckTest extends CheckTestSupport {

    private static final String BASE = "/it/aboutbits/checkstyle/jspecifyInlineTypeUse/";

    @Test
    void inlineMethodReturnPasses() throws Exception {
        assertThat(runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "GoodInlineMethod.java")).isEmpty();
    }

    @Test
    void inlineFieldPasses() throws Exception {
        assertThat(runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "GoodInlineField.java")).isEmpty();
    }

    @Test
    void inlineParameterPasses() throws Exception {
        assertThat(runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "GoodInlineParameter.java")).isEmpty();
    }

    @Test
    void inlineLocalPasses() throws Exception {
        assertThat(runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "GoodInlineLocal.java")).isEmpty();
    }

    @Test
    void annotationOnLineAboveMethodFails() throws Exception {
        var v = runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "BadAboveMethod.java");
        assertThat(v).hasSize(1);
    }

    @Test
    void annotationOnLineAboveFieldFails() throws Exception {
        var v = runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "BadAboveField.java");
        assertThat(v).hasSize(1);
    }

    @Test
    void annotationOnLineAboveParameterFails() throws Exception {
        var v = runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "BadAboveParameter.java");
        assertThat(v).hasSize(1);
    }

    @Test
    void annotationOnLineAboveLocalFails() throws Exception {
        var v = runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "BadAboveLocal.java");
        assertThat(v).hasSize(1);
    }

    @Test
    void nonJspecifyNullableImportedFromElsewhereIsIgnored() throws Exception {
        assertThat(runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "NonJSpecifyNullableAboveFieldIgnored.java")).isEmpty();
    }

    @Test
    void nonJspecifyFullyQualifiedNullableIsIgnored() throws Exception {
        assertThat(runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "NonJSpecifyFqnNullableAboveFieldIgnored.java")).isEmpty();
    }

    @Test
    void fullyQualifiedJspecifyNullableAboveFieldFails() throws Exception {
        var v = runCheck(JSpecifyInlineTypeUseCheck.class, BASE + "JSpecifyFqnAboveFieldFails.java");
        assertThat(v).hasSize(1);
    }
}
