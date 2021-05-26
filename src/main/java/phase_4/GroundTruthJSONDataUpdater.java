// PHASE 4

package phase_4;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeSet;

public class GroundTruthJSONDataUpdater {

    public void readGroundTruthPhase2JsonData() throws IOException {

        String lineNumber;
        String groundTruthDataPhase3path = "ground_truth_phase_3.json";
        String groundTruthDataPhase4Data = new String((Files.readAllBytes(Paths.get(groundTruthDataPhase3path))));
        JSONArray resolutionVersionObjects = new JSONArray(groundTruthDataPhase4Data);

        for (int i = 0; i < resolutionVersionObjects.length(); i++) {

            JSONObject resolutionVersionObject = resolutionVersionObjects.getJSONObject(i);
            Set<JSONObject> resolutionVersionObjectKeys = resolutionVersionObject.keySet();

            for (Object resolutionVersionObjectKey : resolutionVersionObjectKeys) {

                String resolutionVersion = (String) resolutionVersionObjectKey;
                System.out.println("Now processing: " + resolutionVersion);
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
                                JSONObject lineNumberObject = (JSONObject) fileSuffixObject.get(filePathSuffix);
                                Set<JSONObject> lineNumberObjectKeys = lineNumberObject.keySet();
                                String filePathPrefix = "E:/Year 1 Project Dataset/Dataset/" + resolutionVersion + "/";
                                String sourceFilePath = filePathPrefix + filePathSuffix;
                                TreeSet<Integer> modifiedLines = new TreeSet<>();
                                File sourceFile = new File(sourceFilePath);

                                for (Object lineNumberObjectKey : lineNumberObjectKeys) {

                                    lineNumber = (String) lineNumberObjectKey;
                                    JSONObject methodSignatureAndClassNameObject = (JSONObject) lineNumberObject.get(lineNumber);

                                    if (sourceFile.exists()) {

                                        modifiedLines.add(Integer.parseInt(lineNumber));
                                        LinkedHashMap<Integer, LinkedHashMap<String, String>> lineNumberAndMethodSignatureAndClassName = new SourcefileObjectBuilder().buildSourceFileObject(sourceFilePath, resolutionVersion, modifiedLines);
                                        int lineNumber_int = Integer.parseInt(lineNumber);
                                        LinkedHashMap<String, String> methodSignatureAndClassName = lineNumberAndMethodSignatureAndClassName.get(lineNumber_int);

                                        if (methodSignatureAndClassName != null) {

                                            String className = methodSignatureAndClassName.get("className");
                                            String methodSignature = methodSignatureAndClassName.get("methodSignature");
                                            methodSignatureAndClassNameObject.put("className", className);
                                            methodSignatureAndClassNameObject.put("methodSignature", methodSignature);

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // FileWriter fileWriter = new FileWriter("ground_truth_phase_4.json");
        // fileWriter.write(resolutionVersionObjects.toString());
        // fileWriter.close();
        System.out.println("Phase 4 completed!");
    }
}
