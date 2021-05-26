package classes;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.util.List;
import java.util.TreeSet;

public class Sourcefile {

    private String resolutionVersion;
    private String filepath;
    private String sourceCodeContent;
    private List<ClassOrInterfaceDeclaration> classDeclarations;
    private List<MethodDeclaration> methodDeclarations;
    private TreeSet<Integer> modifiedLines;

    public String getResolutionVersion() {
        return resolutionVersion;
    }

    public void setResolutionVersion(String resolutionVersion) {
        this.resolutionVersion = resolutionVersion;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getSourceCodeContent() {
        return sourceCodeContent;
    }

    public void setSourceCodeContent(String sourceCodeContent) {
        this.sourceCodeContent = sourceCodeContent;
    }

    public List<ClassOrInterfaceDeclaration> getClassDeclarations() {
        return classDeclarations;
    }

    public void setClassDeclarations(List<ClassOrInterfaceDeclaration> classDeclarations) {
        this.classDeclarations = classDeclarations;
    }

    public List<MethodDeclaration> getMethodDeclarations() {
        return methodDeclarations;
    }

    public void setMethodDeclarations(List<MethodDeclaration> methodDeclarations) {
        this.methodDeclarations = methodDeclarations;
    }

    public TreeSet<Integer> getModifiedLines() {
        return modifiedLines;
    }

    public void setModifiedLines(TreeSet<Integer> modifiedLines) {
        this.modifiedLines = modifiedLines;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classDeclarations == null) ? 0 : classDeclarations.hashCode());
        result = prime * result + ((filepath == null) ? 0 : filepath.hashCode());
        result = prime * result + ((methodDeclarations == null) ? 0 : methodDeclarations.hashCode());
        result = prime * result + ((modifiedLines == null) ? 0 : modifiedLines.hashCode());
        result = prime * result + ((resolutionVersion == null) ? 0 : resolutionVersion.hashCode());
        result = prime * result + ((sourceCodeContent == null) ? 0 : sourceCodeContent.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Sourcefile other = (Sourcefile) obj;
        if (classDeclarations == null) {
            if (other.classDeclarations != null)
                return false;
        } else if (!classDeclarations.equals(other.classDeclarations))
            return false;
        if (filepath == null) {
            if (other.filepath != null)
                return false;
        } else if (!filepath.equals(other.filepath))
            return false;
        if (methodDeclarations == null) {
            if (other.methodDeclarations != null)
                return false;
        } else if (!methodDeclarations.equals(other.methodDeclarations))
            return false;
        if (modifiedLines == null) {
            if (other.modifiedLines != null)
                return false;
        } else if (!modifiedLines.equals(other.modifiedLines))
            return false;
        if (resolutionVersion == null) {
            if (other.resolutionVersion != null)
                return false;
        } else if (!resolutionVersion.equals(other.resolutionVersion))
            return false;
        if (sourceCodeContent == null) {
            if (other.sourceCodeContent != null)
                return false;
        } else if (!sourceCodeContent.equals(other.sourceCodeContent))
            return false;
        return true;
    }

}
