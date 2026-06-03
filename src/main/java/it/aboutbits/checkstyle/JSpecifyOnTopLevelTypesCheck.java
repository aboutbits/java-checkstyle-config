package it.aboutbits.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

import java.util.Set;

@NullMarked
public class JSpecifyOnTopLevelTypesCheck extends AbstractCheck {

    public static final String MSG_KEY = "jspecify.toplevel.missing";

    private static final Set<String> JSPECIFY_TYPE_ANNOTATIONS = Set.of(
            NullMarked.class.getSimpleName(),
            NullUnmarked.class.getSimpleName()
    );

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
        if (!isTopLevel(ast)) {
            return;
        }
        var modifiers = ast.findFirstToken(TokenTypes.MODIFIERS);
        if (modifiers == null) {
            logMissing(ast);
            return;
        }
        for (var child = modifiers.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getType() != TokenTypes.ANNOTATION) {
                continue;
            }
            var name = AnnotationNames.simpleName(child);
            if (name != null && JSPECIFY_TYPE_ANNOTATIONS.contains(name)) {
                return;
            }
        }
        logMissing(ast);
    }

    private void logMissing(DetailAST ast) {
        var name = ast.findFirstToken(TokenTypes.IDENT);
        log(ast, MSG_KEY, name != null ? name.getText() : "");
    }

    private static boolean isTopLevel(DetailAST ast) {
        var parent = ast.getParent();
        return parent == null || parent.getType() == TokenTypes.COMPILATION_UNIT;
    }
}
