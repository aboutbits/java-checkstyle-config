package it.aboutbits.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.ArrayList;

public class JspecifyMapStructMapperAnnotationCheck extends AbstractCheck {

    public static final String MSG_KEY = "jspecify.mapstruct.annotation.invalid";

    private String mapperAnnotationName = "Mapper";
    private String annotateWithAnnotationName = "AnnotateWith";
    private String nullUnmarkedAnnotationName = "NullUnmarked";

    public void setMapperAnnotationName(String name) {
        this.mapperAnnotationName = name;
    }

    public void setAnnotateWithAnnotationName(String name) {
        this.annotateWithAnnotationName = name;
    }

    public void setNullUnmarkedAnnotationName(String name) {
        this.nullUnmarkedAnnotationName = name;
    }

    @Override
    public int[] getDefaultTokens() {
        return new int[]{TokenTypes.INTERFACE_DEF};
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
        var last = annotations.get(annotations.size() - 1);
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

    private static String memberValuePairClassLiteralName(DetailAST pair) {
        var ident = pair.findFirstToken(TokenTypes.IDENT);
        if (ident == null || !"value".equals(ident.getText())) {
            return null;
        }
        var expr = pair.findFirstToken(TokenTypes.EXPR);
        return expr == null ? null : classLiteralName(expr);
    }

    private static String classLiteralName(DetailAST expr) {
        var dot = expr.findFirstToken(TokenTypes.DOT);
        if (dot == null) {
            return null;
        }
        var classLiteral = dot.findFirstToken(TokenTypes.LITERAL_CLASS);
        if (classLiteral == null) {
            return null;
        }
        var ident = dot.findFirstToken(TokenTypes.IDENT);
        if (ident != null) {
            return ident.getText();
        }
        var nestedDot = dot.findFirstToken(TokenTypes.DOT);
        if (nestedDot != null) {
            var last = nestedDot.getLastChild();
            if (last != null && last.getType() == TokenTypes.IDENT) {
                return last.getText();
            }
        }
        return null;
    }
}
