package phase_4;

import classes.Sourcefile;
import com.github.javaparser.Position;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import ucl.cdt.cybersecurity.App;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeSet;

public class MethodSignatureAndClassNameDeterminer {

    public LinkedHashMap<Integer, LinkedHashMap<String, String>> getMethodSignatureAndClassName(Sourcefile sourcefile) {

        LinkedHashMap<Integer, LinkedHashMap<String, String>> lineNumberAndMethodSignatureAndClassName = new LinkedHashMap<>();
        TreeSet<Integer> modifiedLines = sourcefile.getModifiedLines();
        List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = sourcefile.getClassDeclarations();
        String content = sourcefile.getSourceCodeContent();
        String className = null;
        String methodSignature = null;
        HashSet<String> keysForWhollyAddedMethods = App.getKeysForWhollyAddedMethods();

        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // The code below identifies new files. While newly added files count as vulnerability fixes, we are only interested in identifying vulnerable methods so they are not fit for our purpose, hence we do not consider them.
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        content = content.trim();
        long totalNumberOfLinesInSourceFile = content.trim().chars().filter(x -> x == '\n').count() + 1;
        TreeSet<Integer> numberOfLinesInSourceFile = new TreeSet<>();

        for (int i = 0; i < totalNumberOfLinesInSourceFile; i++) {
            numberOfLinesInSourceFile.add(i + 1);
        }
        //------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        if (modifiedLines.equals(numberOfLinesInSourceFile)) {
            // New file detected! - We insert 'no_class_name' and 'no_method_signature', so that the elements related to the new file will be filtered out when writing our final JSON in Phase 5.
            for (Integer modifiedLine : modifiedLines) {
                LinkedHashMap<String, String> methodSignatureAndClassName = new LinkedHashMap<>();
                className = "no_class_name";
                methodSignature = "no_method_signature";
                methodSignatureAndClassName.put("methodSignature", methodSignature);
                methodSignatureAndClassName.put("className", className);
                lineNumberAndMethodSignatureAndClassName.put(modifiedLine, methodSignatureAndClassName);
            }
        } else {

            for (Integer modifiedLine : modifiedLines) {

                LinkedHashMap<String, String> methodSignatureAndClassName = new LinkedHashMap<>();

                for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classOrInterfaceDeclarations) {

                    Position classBeginPosition = classOrInterfaceDeclaration.getBegin().get();
                    Position classEndPosition = classOrInterfaceDeclaration.getEnd().get();
                    int classBeginLine = classBeginPosition.line;
                    int classEndLine = classEndPosition.line;

                    if ((modifiedLine >= classBeginLine) && (modifiedLine <= classEndLine)) {
                        className = classOrInterfaceDeclaration.getNameAsString();
                    }

                    List<MethodDeclaration> methodDeclarations = classOrInterfaceDeclaration.getMethods();

                    for (MethodDeclaration methodDeclaration : methodDeclarations) {

                        Position methodBeginPosition = methodDeclaration.getBegin().get();
                        Position methodEndPosition = methodDeclaration.getEnd().get();

                        int methodBeginLine = methodBeginPosition.line;
                        int methodEndLine = methodEndPosition.line;

                        if ((modifiedLine >= methodBeginLine) && (modifiedLine <= methodEndLine)) {
                            methodSignature = methodDeclaration.getSignature().toString();
                        }

                        // Check for wholly added methods. Setting the methodSignature to null will result in the filtering out of any wholly added method.
                        String key = sourcefile.getFilepath() + "=+=" + classOrInterfaceDeclaration.getNameAsString() + "=+=" + methodDeclaration.getSignature().toString();

                        if (keysForWhollyAddedMethods.contains(key)) {
                            methodSignature = null;
                        }
                    }

                    if (className == null) {
                        className = "no_class_name";
                    }
                    if (methodSignature == null) {
                        methodSignature = "no_method_signature";
                    }
                    methodSignatureAndClassName.put("methodSignature", methodSignature);
                    methodSignatureAndClassName.put("className", className);
                }
                lineNumberAndMethodSignatureAndClassName.put(modifiedLine, methodSignatureAndClassName);
            }
        }
        return lineNumberAndMethodSignatureAndClassName;
    }
}