package phase_5;

import classes.Sourcefile;
import com.github.javaparser.Position;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import ucl.cdt.cybersecurity.App;

import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class KeyBuilder {

    public void buildKeysForWhollyAddedMethods (Sourcefile sourcefile) {

        HashSet<String> keysForWhollyAddedMethods = App.getKeysForWhollyAddedMethods();
        TreeSet<Integer> modifiedLines = sourcefile.getModifiedLines();
        List<ClassOrInterfaceDeclaration> classOrInterfaceDeclarations = sourcefile.getClassDeclarations();

        for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : classOrInterfaceDeclarations) {

            List<MethodDeclaration> methodDeclarations = classOrInterfaceDeclaration.getMethods();

            for (MethodDeclaration methodDeclaration : methodDeclarations) {

                Position methodBeginPosition = methodDeclaration.getBegin().get();
                Position methodEndPosition = methodDeclaration.getEnd().get();

                int methodBeginLine = methodBeginPosition.line;
                int methodEndLine = methodEndPosition.line;

                //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                // The code below identifies wholly added methods. We ignore these types of modifications (by setting 'methodSignature) because we cannot consider these newly added methods faulty as they did not exist at all before the fix.
                //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
                TreeSet<Integer> methodLines = new TreeSet<>();

                for (int line = methodBeginLine; line <= methodEndLine; line++) {
                    methodLines.add(line);
                }

                if (modifiedLines.containsAll(methodLines)) {
                    String key = sourcefile.getFilepath() + "=+=" + classOrInterfaceDeclaration.getNameAsString() + "=+=" + methodDeclaration.getSignature().toString();
                    keysForWhollyAddedMethods.add(key);
                }
                //----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            }
        }
    }
}
