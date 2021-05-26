package classes;

import java.util.HashSet;

public class ResolutionVersion {

    private String resolutionVersion;
    private HashSet<Vulnerability> vulnerabilities;

    public String getResolutionVersion() {
        return resolutionVersion;
    }

    public void setResolutionVersion(String resolutionVersion) {
        this.resolutionVersion = resolutionVersion;
    }

    public HashSet<Vulnerability> getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(HashSet<Vulnerability> vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resolutionVersion == null) ? 0 : resolutionVersion.hashCode());
        result = prime * result + ((vulnerabilities == null) ? 0 : vulnerabilities.hashCode());
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
        ResolutionVersion other = (ResolutionVersion) obj;
        if (resolutionVersion == null) {
            if (other.resolutionVersion != null)
                return false;
        } else if (!resolutionVersion.equals(other.resolutionVersion))
            return false;
        if (vulnerabilities == null) {
            if (other.vulnerabilities != null)
                return false;
        } else if (!vulnerabilities.equals(other.vulnerabilities))
            return false;
        return true;
    }

}
