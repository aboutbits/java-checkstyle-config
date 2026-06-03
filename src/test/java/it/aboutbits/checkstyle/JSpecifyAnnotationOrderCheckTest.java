package it.aboutbits.checkstyle;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class JSpecifyAnnotationOrderCheckTest extends CheckTestSupport {

    private static final String BASE = "/it/aboutbits/checkstyle/jspecifyAnnotationOrder/";

    @Test
    void jspecifyAnnotationAsLastPasses() throws Exception {
        var v = runCheck(JSpecifyAnnotationOrderCheck.class, BASE + "GoodLast.java");
        assertThat(v).isEmpty();
    }

    @Test
    void jspecifyAnnotationFollowedByOtherFailsAndHighlightsCloseAnnotation() throws Exception {
        var v = runCheck(JSpecifyAnnotationOrderCheck.class, BASE + "BadInMiddle.java");
        assertThat(v).hasSize(1);
        assertThat(v.getFirst()).contains(NullMarked.class.getSimpleName());
        assertThat(v.getFirst()).startsWith("6:");
    }

    @Test
    void jspecifyAnnotationAsFirstFailsAndHighlightsCloseAnnotation() throws Exception {
        var v = runCheck(JSpecifyAnnotationOrderCheck.class, BASE + "BadFirst.java");
        assertThat(v).hasSize(1);
        assertThat(v.getFirst()).startsWith("5:");
    }

    @Test
    void noJspecifyAnnotationDoesNotFire() throws Exception {
        var v = runCheck(JSpecifyAnnotationOrderCheck.class, BASE + "NoJSpecify.java");
        assertThat(v).isEmpty();
    }

    @Test
    void customCloseAnnotationsArePicked() throws Exception {
        var properties = Map.of("closeAnnotations", "Custom,AnotherClose");

        var ok = runCheck(JSpecifyAnnotationOrderCheck.class, BASE + "CustomCloseLast.java", properties);
        assertThat(ok).isEmpty();

        var bad = runCheck(JSpecifyAnnotationOrderCheck.class, BASE + "CustomCloseInMiddle.java", properties);
        assertThat(bad).hasSize(1);
        assertThat(bad.getFirst()).contains("Custom");
    }

    @Test
    void closeAnnotationsAreTrimmed() throws Exception {
        var v = runCheck(
                JSpecifyAnnotationOrderCheck.class,
                BASE + "GoodLast.java",
                Map.of("closeAnnotations", "  NullMarked , NullUnmarked  ")
        );
        assertThat(v).isEmpty();
    }
}
