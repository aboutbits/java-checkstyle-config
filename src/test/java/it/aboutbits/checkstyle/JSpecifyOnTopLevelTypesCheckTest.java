package it.aboutbits.checkstyle;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class JSpecifyOnTopLevelTypesCheckTest extends CheckTestSupport {

    private static final String BASE = "/it/aboutbits/checkstyle/jspecifyOnTopLevelTypes/";

    @Test
    void classMarkedWithNullMarkedPasses() throws Exception {
        var v = runCheck(JSpecifyOnTopLevelTypesCheck.class, BASE + "GoodMarked.java");
        assertThat(v).isEmpty();
    }

    @Test
    void classMarkedWithNullUnmarkedPasses() throws Exception {
        var v = runCheck(JSpecifyOnTopLevelTypesCheck.class, BASE + "GoodUnmarked.java");
        assertThat(v).isEmpty();
    }

    @Test
    void fullyQualifiedJspecifyAnnotationPasses() throws Exception {
        var v = runCheck(JSpecifyOnTopLevelTypesCheck.class, BASE + "GoodFullyQualified.java");
        assertThat(v).isEmpty();
    }

    @Test
    void classWithoutJspecifyAnnotationFails() throws Exception {
        var v = runCheck(JSpecifyOnTopLevelTypesCheck.class, BASE + "BadMissing.java");
        assertThat(v).hasSize(1);
        assertThat(v.getFirst()).contains("BadMissing");
    }

    @Test
    void annotationDeclarationIsExempt() throws Exception {
        var v = runCheck(JSpecifyOnTopLevelTypesCheck.class, BASE + "AnnotationDefExempt.java");
        assertThat(v).isEmpty();
    }

    @Test
    void nestedClassIsNotChecked() throws Exception {
        var v = runCheck(JSpecifyOnTopLevelTypesCheck.class, BASE + "NestedClassOnly.java");
        assertThat(v).isEmpty();
    }

    @Test
    void recordWithoutJspecifyFails() throws Exception {
        var v = runCheck(JSpecifyOnTopLevelTypesCheck.class, BASE + "BadRecord.java");
        assertThat(v).hasSize(1);
        assertThat(v.getFirst()).contains("BadRecord");
    }
}
