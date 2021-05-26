// jsoup: https://jsoup.org/

package ucl.cdt.cybersecurity;

import phase_5.VulnerableComponentsProcessor;

import java.io.IOException;
import java.text.ParseException;

public class App {

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        // new HTMLDataExtractor().buildTomcat7VulnerabilitiesJsonData(); // Phase 1
        // new PythonScriptRunner().runPythonScript(); // Phase 2
        // new SVNLineNumberExtractor().getSVNLineNumber(); // Phase 3
        // new GroundTruthJSONDataUpdater().readGroundTruthPhase2JsonData(); // Phase 4
        new VulnerableComponentsProcessor().processVulnerableComponents(); // Phase 5
    }

}