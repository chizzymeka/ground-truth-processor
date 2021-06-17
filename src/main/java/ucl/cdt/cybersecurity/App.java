// jsoup: https://jsoup.org/

package ucl.cdt.cybersecurity;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;

public class App {

    private static HashSet<String> keysForWhollyAddedMethods = new HashSet<>();

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        // new HTMLDataExtractor().buildTomcat7VulnerabilitiesJsonData(); // Phase 1
        // new PythonScriptRunner().runPythonScript(); // Phase 2
        // new SVNLineNumberExtractor().getSVNLineNumber(); // Phase 3
        // new GroundTruthJSONDataUpdater().readGroundTruthPhase2JsonData(); // Phase 4

        /*
            ----- PHASE 5 - ORDER OF EXECUTION -----
            new SourceFileObjectBuilder().buildKeysForWhollyAddedMethods();
                ↓
            new KeyBuilder().buildKeysForWhollyAddedMethods(sourcefile);
                ↓
            new VulnerableComponentsProcessor().processVulnerableComponents();
                ↓
            new SourceFileObjectInitializer().buildSourceFileObject(sourceFilePath, resolutionVersion, modifiedLines);
                ↓
            new MethodSignatureAndClassNameDeterminer().getMethodSignatureAndClassName(sourcefile);
        */

        // new SourceFileObjectBuilder().buildKeysForWhollyAddedMethods(); // Phase 5
        // new VulnerableComponentsProcessor().processVulnerableComponents(); // Phase 5
    }

    public static HashSet<String> getKeysForWhollyAddedMethods() {
        return keysForWhollyAddedMethods;
    }

    public static void setKeysForWhollyAddedMethods(HashSet<String> keysForWhollyAddedMethods) {
        App.keysForWhollyAddedMethods = keysForWhollyAddedMethods;
    }
}