// Package com.github.javaparser.ast.visitor: https://javadoc.io/doc/com.github.javaparser/javaparser-core/3.3.1/com/github/javaparser/ast/visitor/package-summary.html

package java_parser;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MethodDeclarationVisitor extends GenericListVisitorAdapter<MethodDeclaration, Void> {

    @Override
    public List<MethodDeclaration> visit(MethodDeclaration methodDeclaration, Void arg) {

        super.visit(methodDeclaration, arg);
        List<MethodDeclaration> methodDeclarations = new ArrayList<>();
        methodDeclarations.add(methodDeclaration);

        return methodDeclarations;
    }
}
