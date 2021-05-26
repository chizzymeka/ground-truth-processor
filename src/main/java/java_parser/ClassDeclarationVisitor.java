package java_parser;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClassDeclarationVisitor extends GenericListVisitorAdapter<ClassOrInterfaceDeclaration, Void> {

    @Override
    public List<ClassOrInterfaceDeclaration> visit(ClassOrInterfaceDeclaration classDeclaration, Void arg) {

        super.visit(classDeclaration, arg);
        List<ClassOrInterfaceDeclaration> classDeclarations = new ArrayList<>();
        classDeclarations.add(classDeclaration);

        return classDeclarations;
    }
}
