package it.aboutbits.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@NullMarked
public class JSpecifyInlineTypeUseCheck extends AbstractCheck {

    public static final String MSG_KEY = "jspecify.inline.notInline";

    private static final String JSPECIFY_PACKAGE = Nullable.class.getPackageName();
    private static final String JSPECIFY_PACKAGE_DOT = JSPECIFY_PACKAGE + ".";
    private static final Set<String> TYPE_USE_ANNOTATIONS = Set.of(
            Nullable.class.getSimpleName(),
            NonNull.class.getSimpleName()
    );

    private Set<String> jspecifySimpleNamesInScope = new HashSet<>();

    @Override
    public int[] getDefaultTokens() {
        return new int[]{
                TokenTypes.METHOD_DEF,
                TokenTypes.VARIABLE_DEF,
                TokenTypes.PARAMETER_DEF,
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
    public void beginTree(DetailAST rootAST) {
        jspecifySimpleNamesInScope = new HashSet<>();
        for (var node = rootAST.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node.getType() != TokenTypes.IMPORT) {
                continue;
            }
            var first = node.getFirstChild();
            if (first == null) {
                continue;
            }
            var text = FullIdent.createFullIdent(first).getText();
            if (text.equals(JSPECIFY_PACKAGE + ".*")) {
                jspecifySimpleNamesInScope.addAll(TYPE_USE_ANNOTATIONS);
            } else if (text.startsWith(JSPECIFY_PACKAGE_DOT)) {
                var simple = text.substring(JSPECIFY_PACKAGE_DOT.length());
                if (TYPE_USE_ANNOTATIONS.contains(simple)) {
                    jspecifySimpleNamesInScope.add(simple);
                }
            }
        }
    }

    @Override
    public void visitToken(DetailAST ast) {
        var modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);
        var type = ast.findFirstToken(TokenTypes.TYPE);
        if (modifiers == null || type == null) {
            return;
        }
        for (var child = modifiers.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getType() != TokenTypes.ANNOTATION) {
                continue;
            }
            var simple = AnnotationNames.simpleName(child);
            if (simple == null || !TYPE_USE_ANNOTATIONS.contains(simple)) {
                continue;
            }
            if (!isJspecifyAnnotation(child, simple)) {
                continue;
            }
            if (child.getLineNo() < type.getLineNo()) {
                log(child, MSG_KEY, simple);
            }
        }
    }

    private boolean isJspecifyAnnotation(DetailAST annotation, String simpleName) {
        var fullName = AnnotationNames.fullName(annotation);
        if (fullName == null) {
            return false;
        }
        if (fullName.contains(".")) {
            return fullName.equals(JSPECIFY_PACKAGE_DOT + simpleName);
        }
        return jspecifySimpleNamesInScope.contains(simpleName);
    }
}
