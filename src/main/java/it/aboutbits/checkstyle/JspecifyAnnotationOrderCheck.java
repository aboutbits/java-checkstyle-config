package it.aboutbits.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.LinkedHashSet;
import java.util.Set;

public class JspecifyAnnotationOrderCheck extends AbstractCheck {

    public static final String MSG_KEY = "jspecify.order.notLast";

    private Set<String> closeAnnotations = new LinkedHashSet<>(Set.of("NullMarked", "NullUnmarked"));

    public void setCloseAnnotations(String... names) {
        var next = new LinkedHashSet<String>();
        for (var n : names) {
            next.add(n.trim());
        }
        this.closeAnnotations = next;
    }

    @Override
    public int[] getDefaultTokens() {
        return new int[]{
                TokenTypes.CLASS_DEF,
                TokenTypes.INTERFACE_DEF,
                TokenTypes.ENUM_DEF,
                TokenTypes.RECORD_DEF,
                };
    }

    @Override
    public int[] getAcceptableTokens() {
        return getDefaultTokens();
    }

    @Override
    public int[] getRequiredTokens() {
        return getDefaultTokens();
    }

    @Override
    public void visitToken(DetailAST ast) {
        var modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);
        if (modifiers == null) {
            return;
        }
        DetailAST lastAnnotation = null;
        var hasCloseAnnotation = false;
        String firstCloseName = null;
        for (var child = modifiers.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getType() != TokenTypes.ANNOTATION) {
                continue;
            }
            var name = AnnotationNames.simpleName(child);
            lastAnnotation = child;
            if (name != null && closeAnnotations.contains(name)) {
                hasCloseAnnotation = true;
                if (firstCloseName == null) {
                    firstCloseName = name;
                }
            }
        }
        if (!hasCloseAnnotation || lastAnnotation == null) {
            return;
        }
        var lastName = AnnotationNames.simpleName(lastAnnotation);
        if (lastName == null || !closeAnnotations.contains(lastName)) {
            log(lastAnnotation, MSG_KEY, firstCloseName);
        }
    }
}
