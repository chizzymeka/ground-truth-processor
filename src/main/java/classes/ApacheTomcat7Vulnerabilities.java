package classes;

import java.util.HashSet;

public class ApacheTomcat7Vulnerabilities {

    private HashSet<ResolutionVersion> resolutionVersions;

    public HashSet<ResolutionVersion> getResolutionVersions() {
        return resolutionVersions;
    }

    public void setResolutionVersions(HashSet<ResolutionVersion> resolutionVersions) {
        this.resolutionVersions = resolutionVersions;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resolutionVersions == null) ? 0 : resolutionVersions.hashCode());
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
        ApacheTomcat7Vulnerabilities other = (ApacheTomcat7Vulnerabilities) obj;
        if (resolutionVersions == null) {
            if (other.resolutionVersions != null)
                return false;
        } else if (!resolutionVersions.equals(other.resolutionVersions))
            return false;
        return true;
    }

}
