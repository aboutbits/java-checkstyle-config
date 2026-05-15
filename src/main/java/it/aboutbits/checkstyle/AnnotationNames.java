package it.aboutbits.checkstyle;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FullIdent;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

final class AnnotationNames {

    private AnnotationNames() {
    }

    /**
     * Returns the simple name of an ANNOTATION node, regardless of whether the
     * source uses an imported name (@Foo) or a fully-qualified name (@a.b.Foo).
     */
    static String simpleName(DetailAST annotation) {
        var ident = annotation.findFirstToken(TokenTypes.IDENT);
        if (ident != null) {
            return ident.getText();
        }
        var dot = annotation.findFirstToken(TokenTypes.DOT);
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
    static String fullName(DetailAST annotation) {
        var child = annotation.findFirstToken(TokenTypes.IDENT);
        if (child == null) {
            child = annotation.findFirstToken(TokenTypes.DOT);
        }
        return child == null ? null : FullIdent.createFullIdent(child).getText();
    }
}
