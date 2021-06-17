package phase_5;

import core.SourceFileObjectInitializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;

public class SourceFileObjectBuilder {

    public void buildKeysForWhollyAddedMethods () throws IOException {

        String lineNumber;
        String groundTruthDataPhase4path = "ground_truth_phase_4.json";
        String groundTruthDataPhase5Data = new String((Files.readAllBytes(Paths.get(groundTruthDataPhase4path))));
        JSONArray resolutionVersionObjects = new JSONArray(groundTruthDataPhase5Data);

        for (int i = 0; i < resolutionVersionObjects.length(); i++) {

            JSONObject resolutionVersionObject = resolutionVersionObjects.getJSONObject(i);
            Set<JSONObject> resolutionVersionObjectKeys = resolutionVersionObject.keySet();

            for (Object resolutionVersionObjectKey : resolutionVersionObjectKeys) {

                String resolutionVersion = (String) resolutionVersionObjectKey;
                System.out.println("Building keys for: " + resolutionVersion);
                JSONObject cveIdObject = (JSONObject) resolutionVersionObject.get(resolutionVersion);
                Set<JSONObject> cveIdObjectKeys = cveIdObject.keySet();

                for (Object cveIdObjectKey : cveIdObjectKeys) {

                    String cveId = (String) cveIdObjectKey;
                    JSONObject cveIdObj = (JSONObject) cveIdObject.get(cveId);
                    JSONObject commitObject = (JSONObject) cveIdObj.get("vulnerabilityFixLocations");
                    Set<JSONObject> commitObjectKeys = commitObject.keySet();

                    for (Object commitObjectKey : commitObjectKeys) {

                        String commitId = (String) commitObjectKey;
                        JSONObject commitUrlObject = (JSONObject) commitObject.get(commitId);
                        Set<JSONObject> commitUrlObjectKeys = commitUrlObject.keySet();

                        for (Object commitUrlObjectKey : commitUrlObjectKeys) {

                            String commitUrl = (String) commitUrlObjectKey;
                            JSONObject fileSuffixObject = (JSONObject) commitUrlObject.get(commitUrl);
                            Set<JSONObject> filePathSuffixObjectKeys = fileSuffixObject.keySet();

                            for (Object filePathSuffixObjectKey : filePathSuffixObjectKeys) {

                                String filePathSuffix = (String) filePathSuffixObjectKey;

                                // Skip test directories. This is based on the assumption that test code cannot be considered vulnerable.
                                if (filePathSuffix.startsWith("test/")) {
                                    continue;
                                }

                                JSONObject lineNumberObject = (JSONObject) fileSuffixObject.get(filePathSuffix);
                                Set<JSONObject> lineNumberObjectKeys = lineNumberObject.keySet();

                                String filePathPrefix = "E:/Year 1 Project Dataset/Dataset/" + resolutionVersion + "/";
                                String sourceFilePath = filePathPrefix + filePathSuffix;
                                TreeSet<Integer> modifiedLines = new TreeSet<>();
                                File sourceFile = new File(sourceFilePath);

                                for (Object lineNumberObjectKey : lineNumberObjectKeys) {

                                    lineNumber = (String) lineNumberObjectKey;

                                    // Ensure that the path is exists. For example, this if-statement will clearly not run if the path directs to a removable drive.
                                    if (sourceFile.exists()) {
                                        modifiedLines.add(Integer.parseInt(lineNumber));
                                        new SourceFileObjectInitializer().buildSourceFileObjectKey(sourceFilePath, resolutionVersion, modifiedLines);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
