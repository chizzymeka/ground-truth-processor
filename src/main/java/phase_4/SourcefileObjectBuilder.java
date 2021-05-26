package phase_4;

import classes.Sourcefile;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import java_parser.ClassDeclarationVisitor;
import java_parser.MethodDeclarationVisitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeSet;

public class SourcefileObjectBuilder {

    public LinkedHashMap<Integer, LinkedHashMap<String, String>> buildSourceFileObject(String sourcefilePath, String resolutionVersion, TreeSet<Integer> modifiedLines) throws IOException {

        Sourcefile sourcefile = new Sourcefile();
        sourcefile.setResolutionVersion(resolutionVersion);
        sourcefile.setFilepath(sourcefilePath);
        String content = new SourcefileObjectBuilder().getSourcefileContent(sourcefilePath);
        sourcefile.setSourceCodeContent(content);
        sourcefile.setModifiedLines(modifiedLines);

        CompilationUnit compilationUnitNode = StaticJavaParser.parse(new File(sourcefilePath));
        List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = new ClassDeclarationVisitor().visit(compilationUnitNode, null);
        sourcefile.setClassDeclarations(classOrInterfaceDeclarations);

        List<MethodDeclaration> methodDeclarations = new MethodDeclarationVisitor().visit(compilationUnitNode, null);
        sourcefile.setMethodDeclarations(methodDeclarations);

        LinkedHashMap<Integer, LinkedHashMap<String, String>> lineNumberAndMethodSignatureAndClassName = new MethodSignatureAndClassNameDeterminer().getMethodSignatureAndClassName(sourcefile, classOrInterfaceDeclarations, methodDeclarations);

        return lineNumberAndMethodSignatureAndClassName;
    }

    String getSourcefileContent(String sourcefilePath) throws IOException {

        File sourcefile = new File(sourcefilePath);
        FileInputStream fileInputStream = new FileInputStream(sourcefile);
        byte[] data = new byte[(int) sourcefile.length()];
        fileInputStream.read(data);
        fileInputStream.close();

        String sourcefileContent = new String(data, "UTF-8");

        return sourcefileContent;
    }
}
