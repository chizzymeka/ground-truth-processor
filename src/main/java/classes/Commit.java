package classes;

public class Commit {

    private String filePathSuffix;
    private String className;
    private String methodSignature;

    public String getFilePathSuffix() {
        return filePathSuffix;
    }

    public void setFilePathSuffix(String filePathSuffix) {
        this.filePathSuffix = filePathSuffix;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(String methodSignature) {
        this.methodSignature = methodSignature;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((className == null) ? 0 : className.hashCode());
        result = prime * result + ((filePathSuffix == null) ? 0 : filePathSuffix.hashCode());
        result = prime * result + ((methodSignature == null) ? 0 : methodSignature.hashCode());
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
        Commit other = (Commit) obj;
        if (className == null) {
            if (other.className != null)
                return false;
        } else if (!className.equals(other.className))
            return false;
        if (filePathSuffix == null) {
            if (other.filePathSuffix != null)
                return false;
        } else if (!filePathSuffix.equals(other.filePathSuffix))
            return false;
        if (methodSignature == null) {
            if (other.methodSignature != null)
                return false;
        } else if (!methodSignature.equals(other.methodSignature))
            return false;
        return true;
    }
}