package it.aboutbits.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;

import java.util.LinkedHashSet;
import java.util.Set;

@NullMarked
public class JSpecifyAnnotationOrderCheck extends AbstractCheck {

    public static final String MSG_KEY = "jspecify.order.notLast";

    private Set<String> closeAnnotations = new LinkedHashSet<>(Set.of(
            NullMarked.class.getSimpleName(),
            NullUnmarked.class.getSimpleName()
    ));

    // Called by Checkstyle via reflection for <property name="closeAnnotations" value="..."/>
    @SuppressWarnings("unused")
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
        DetailAST firstCloseAnnotation = null;
        String firstCloseName = null;
        for (var child = modifiers.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getType() != TokenTypes.ANNOTATION) {
                continue;
            }
            var name = AnnotationNames.simpleName(child);
            lastAnnotation = child;
            if (name != null && closeAnnotations.contains(name) && firstCloseAnnotation == null) {
                firstCloseAnnotation = child;
                firstCloseName = name;
            }
        }
        if (firstCloseAnnotation == null || lastAnnotation == null) {
            return;
        }
        var lastName = AnnotationNames.simpleName(lastAnnotation);
        if (lastName == null || !closeAnnotations.contains(lastName)) {
            log(firstCloseAnnotation, MSG_KEY, firstCloseName);
        }
    }
}
