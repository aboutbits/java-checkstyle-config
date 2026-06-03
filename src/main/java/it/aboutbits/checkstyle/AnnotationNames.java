package it.aboutbits.checkstyle;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
final class AnnotationNames {

    private AnnotationNames() {
    }

    /**
     * Returns the simple name of an ANNOTATION node, regardless of whether the
     * source uses an imported name (@Foo) or a fully-qualified name (@a.b.Foo).
     */
    static @Nullable String simpleName(DetailAST annotation) {
        return lastIdentIn(annotation);
    }

    /**
     * Returns the text of the last IDENT reachable from {@code node}: the direct
     * IDENT child when present, or the last child of the first DOT child otherwise.
     */
    static @Nullable String lastIdentIn(DetailAST node) {
        var ident = node.findFirstToken(TokenTypes.IDENT);
        if (ident != null) {
            return ident.getText();
        }
        var dot = node.findFirstToken(TokenTypes.DOT);
        if (dot != null) {
            var last = dot.getLastChild();
            if (last != null && last.getType() == TokenTypes.IDENT) {
                return last.getText();
            }
        }
        return null;
    }

    /**
     * Returns the source-text name of an ANNOTATION node: the simple name when
     * the source uses an imported form, or the full dotted name when written
     * inline as a fully-qualified annotation.
     */
    static @Nullable String fullName(DetailAST annotation) {
        var child = annotation.findFirstToken(TokenTypes.IDENT);
        if (child == null) {
            child = annotation.findFirstToken(TokenTypes.DOT);
        }
        return child == null ? null : FullIdent.createFullIdent(child).getText();
    }
}
