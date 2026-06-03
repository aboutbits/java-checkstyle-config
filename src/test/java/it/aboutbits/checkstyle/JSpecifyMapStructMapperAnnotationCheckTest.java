package it.aboutbits.checkstyle;

import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@NullMarked
class JSpecifyMapStructMapperAnnotationCheckTest extends CheckTestSupport {

    private static final String BASE = "/it/aboutbits/checkstyle/jspecifyMapStructMapperAnnotation/";

    @Test
    void wellOrderedMapperPasses() throws Exception {
        assertThat(runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "GoodMapper.java")).isEmpty();
    }

    @Test
    void mapperWithNamedValueArgumentPasses() throws Exception {
        assertThat(runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "GoodMapperNamedValue.java")).isEmpty();
    }

    @Test
    void swappedOrderFails() throws Exception {
        var v = runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "BadOrderSwapped.java");
        assertThat(v).hasSize(1);
        assertThat(v.getFirst()).contains("BadOrderSwapped");
    }

    @Test
    void missingAnnotateWithFails() throws Exception {
        var v = runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "BadMissingAnnotateWith.java");
        assertThat(v).hasSize(1);
    }

    @Test
    void annotateWithWrongClassLiteralFails() throws Exception {
        var v = runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "BadWrongClassLiteral.java");
        assertThat(v).hasSize(1);
    }

    @Test
    void interfaceWithoutMapperIsIgnored() throws Exception {
        assertThat(runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "NonMapperInterfaceIgnored.java")).isEmpty();
    }

    @Test
    void customMapperAnnotationNameIsHonored() throws Exception {
        var properties = Map.of("mapperAnnotationName", "MyMapper");

        var ok = runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "CustomMapperGood.java", properties);
        assertThat(ok).isEmpty();

        var bad = runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "CustomMapperBad.java", properties);
        assertThat(bad).hasSize(1);
        assertThat(bad.getFirst()).contains("CustomMapperBad");
    }

    @Test
    void customAnnotateWithAnnotationNameIsHonored() throws Exception {
        var properties = Map.of("annotateWithAnnotationName", "MyAnnotateWith");

        var ok = runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "CustomAnnotateWithGood.java", properties);
        assertThat(ok).isEmpty();

        var bad = runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "GoodMapper.java", properties);
        assertThat(bad).hasSize(1);
    }

    @Test
    void customNullUnmarkedAnnotationNameIsHonored() throws Exception {
        var properties = Map.of("nullUnmarkedAnnotationName", "MyUnmarked");

        var ok = runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "CustomNullUnmarkedGood.java", properties);
        assertThat(ok).isEmpty();

        var bad = runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "GoodMapper.java", properties);
        assertThat(bad).hasSize(1);
    }

    @Test
    void wellOrderedAbstractClassMapperPasses() throws Exception {
        assertThat(runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "GoodAbstractMapper.java")).isEmpty();
    }

    @Test
    void abstractClassMapperWithSwappedOrderFails() throws Exception {
        var v = runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "BadAbstractMapperOrderSwapped.java");
        assertThat(v).hasSize(1);
        assertThat(v.getFirst()).contains("BadAbstractMapperOrderSwapped");
    }

    @Test
    void nonAbstractClassWithMapperAnnotationIsIgnored() throws Exception {
        assertThat(runCheck(JSpecifyMapStructMapperAnnotationCheck.class, BASE + "NonAbstractClassWithMapperAnnotationIgnored.java")).isEmpty();
    }
}
