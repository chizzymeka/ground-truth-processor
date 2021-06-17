package core;

import classes.Sourcefile;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import java_parser.ClassDeclarationVisitor;
import phase_4.MethodSignatureAndClassNameDeterminer;
import phase_5.KeyBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeSet;

public class SourceFileObjectInitializer {

    public void buildSourceFileObjectKey(String sourcefilePath, String resolutionVersion, TreeSet<Integer> modifiedLines) throws IOException {

        Sourcefile sourcefile = new Sourcefile();
        sourcefile.setResolutionVersion(resolutionVersion);
        sourcefile.setFilepath(sourcefilePath);
        String content = new SourceFileObjectInitializer().getSourcefileContent(sourcefilePath);
        sourcefile.setSourceCodeContent(content);
        sourcefile.setModifiedLines(modifiedLines);

        CompilationUnit compilationUnitNode = StaticJavaParser.parse(new File(sourcefilePath));
        List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = new ClassDeclarationVisitor().visit(compilationUnitNode, null);
        sourcefile.setClassDeclarations(classOrInterfaceDeclarations);

        new KeyBuilder().buildKeysForWhollyAddedMethods(sourcefile);
    }

    public LinkedHashMap<Integer, LinkedHashMap<String, String>> buildSourceFileObject(String sourcefilePath, String resolutionVersion, TreeSet<Integer> modifiedLines) throws IOException {

        Sourcefile sourcefile = new Sourcefile();
        sourcefile.setResolutionVersion(resolutionVersion);
        sourcefile.setFilepath(sourcefilePath);
        String content = new SourceFileObjectInitializer().getSourcefileContent(sourcefilePath);
        sourcefile.setSourceCodeContent(content);
        sourcefile.setModifiedLines(modifiedLines);

        CompilationUnit compilationUnitNode = StaticJavaParser.parse(new File(sourcefilePath));
        List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = new ClassDeclarationVisitor().visit(compilationUnitNode, null);
        sourcefile.setClassDeclarations(classOrInterfaceDeclarations);

        LinkedHashMap<Integer, LinkedHashMap<String, String>> lineNumberAndMethodSignatureAndClassName = new MethodSignatureAndClassNameDeterminer().getMethodSignatureAndClassName(sourcefile);

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
