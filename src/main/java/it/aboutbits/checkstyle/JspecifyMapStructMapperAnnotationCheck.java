package it.aboutbits.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.NullUnmarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;

@NullMarked
public class JspecifyMapStructMapperAnnotationCheck extends AbstractCheck {

    public static final String MSG_KEY = "jspecify.mapstruct.annotation.invalid";

    private String mapperAnnotationName = "Mapper";
    private String annotateWithAnnotationName = "AnnotateWith";
    private String nullUnmarkedAnnotationName = NullUnmarked.class.getSimpleName();

    // The three setters below are called by Checkstyle via reflection for the corresponding <property .../> entries.
    @SuppressWarnings("unused")
    public void setMapperAnnotationName(String name) {
        this.mapperAnnotationName = name;
    }

    @SuppressWarnings("unused")
    public void setAnnotateWithAnnotationName(String name) {
        this.annotateWithAnnotationName = name;
    }

    @SuppressWarnings("unused")
    public void setNullUnmarkedAnnotationName(String name) {
        this.nullUnmarkedAnnotationName = name;
    }

    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.INTERFACE_DEF, TokenTypes.CLASS_DEF};
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
        if (ast.getType() == TokenTypes.CLASS_DEF && modifiers.findFirstToken(TokenTypes.ABSTRACT) == null) {
            return;
        }
        var annotations = new ArrayList<DetailAST>();
        var hasMapper = false;
        for (var child = modifiers.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getType() != TokenTypes.ANNOTATION) {
                continue;
            }
            annotations.add(child);
            if (mapperAnnotationName.equals(AnnotationNames.simpleName(child))) {
                hasMapper = true;
            }
        }
        if (!hasMapper) {
            return;
        }
        if (annotations.size() < 2) {
            logInvalid(ast);
            return;
        }
        var last = annotations.getLast();
        var secondLast = annotations.get(annotations.size() - 2);
        var lastOk = nullUnmarkedAnnotationName.equals(AnnotationNames.simpleName(last));
        var secondLastOk = annotateWithAnnotationName.equals(AnnotationNames.simpleName(secondLast))
                && annotateWithArgumentReferences(secondLast, nullUnmarkedAnnotationName);
        if (!lastOk || !secondLastOk) {
            logInvalid(ast);
        }
    }

    private void logInvalid(DetailAST ast) {
        var name = ast.findFirstToken(TokenTypes.IDENT);
        log(ast, MSG_KEY, name != null ? name.getText() : "");
    }

    private static boolean annotateWithArgumentReferences(DetailAST annotation, String expectedClassName) {
        for (var child = annotation.getFirstChild(); child != null; child = child.getNextSibling()) {
            var found = switch (child.getType()) {
                case TokenTypes.EXPR -> classLiteralName(child);
                case TokenTypes.ANNOTATION_MEMBER_VALUE_PAIR -> memberValuePairClassLiteralName(child);
                default -> null;
            };
            if (expectedClassName.equals(found)) {
                return true;
            }
        }
        return false;
    }

    private static @Nullable String memberValuePairClassLiteralName(DetailAST pair) {
        var ident = pair.findFirstToken(TokenTypes.IDENT);
        if (ident == null || !"value".equals(ident.getText())) {
            return null;
        }
        var expr = pair.findFirstToken(TokenTypes.EXPR);
        return expr == null ? null : classLiteralName(expr);
    }

    private static @Nullable String classLiteralName(DetailAST expr) {
        var dot = expr.findFirstToken(TokenTypes.DOT);
        if (dot == null) {
            return null;
        }
        if (dot.findFirstToken(TokenTypes.LITERAL_CLASS) == null) {
            return null;
        }
        return AnnotationNames.lastIdentIn(dot);
    }
}
