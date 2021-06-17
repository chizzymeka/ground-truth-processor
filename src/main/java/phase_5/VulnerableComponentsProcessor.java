package phase_5;

import core.SourceFileObjectInitializer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class VulnerableComponentsProcessor {

    public void processVulnerableComponents() throws IOException {

        String lineNumber;
        String groundTruthDataPhase4path = "ground_truth_phase_4.json";
        String groundTruthDataPhase5Data = new String((Files.readAllBytes(Paths.get(groundTruthDataPhase4path))));
        JSONArray resolutionVersionObjects = new JSONArray(groundTruthDataPhase5Data);
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < resolutionVersionObjects.length(); i++) {

            JSONObject resolutionVersionObject = resolutionVersionObjects.getJSONObject(i);
            Set<JSONObject> resolutionVersionObjectKeys = resolutionVersionObject.keySet();

            JSONObject updatedResolutionVersionObjects = new JSONObject();
            for (Object resolutionVersionObjectKey : resolutionVersionObjectKeys) {

                String resolutionVersion = (String) resolutionVersionObjectKey;
                System.out.println("Now processing: " + resolutionVersion);
                JSONObject cveIdObject = (JSONObject) resolutionVersionObject.get(resolutionVersion);
                Set<JSONObject> cveIdObjectKeys = cveIdObject.keySet();

                JSONObject cveObjects = new JSONObject();
                for (Object cveIdObjectKey : cveIdObjectKeys) {

                    String cveId = (String) cveIdObjectKey;
                    JSONObject cveIdObj = (JSONObject) cveIdObject.get(cveId);
                    JSONObject commitObject = (JSONObject) cveIdObj.get("vulnerabilityFixLocations");
                    Set<JSONObject> commitObjectKeys = commitObject.keySet();

                    JSONObject commitObjects = new JSONObject();
                    for (Object commitObjectKey : commitObjectKeys) {

                        String commitId = (String) commitObjectKey;
                        JSONObject commitUrlObject = (JSONObject) commitObject.get(commitId);
                        Set<JSONObject> commitUrlObjectKeys = commitUrlObject.keySet();

                        JSONObject commitUrlObjects = new JSONObject();
                        for (Object commitUrlObjectKey : commitUrlObjectKeys) {

                            String commitUrl = (String) commitUrlObjectKey;
                            JSONObject fileSuffixObject = (JSONObject) commitUrlObject.get(commitUrl);
                            Set<JSONObject> filePathSuffixObjectKeys = fileSuffixObject.keySet();

                            JSONObject filePathSuffixesObjects = new JSONObject();
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
                                String className = null;
                                String methodSignature;
                                LinkedHashSet<String> methodSignatures = new LinkedHashSet<>();
                                LinkedHashMap<String, LinkedHashSet<String>> vulnerableComponents = new LinkedHashMap<>();

                                JSONObject vulnerableComponentsObjects = new JSONObject();
                                for (Object lineNumberObjectKey : lineNumberObjectKeys) {

                                    lineNumber = (String) lineNumberObjectKey;

                                    // Ensure that the path is exists. For example, this if-statement will clearly not run if the path directs to a removable drive.
                                    if (sourceFile.exists()) {

                                        modifiedLines.add(Integer.parseInt(lineNumber));
                                        LinkedHashMap<Integer, LinkedHashMap<String, String>> lineNumberAndMethodSignatureAndClassName = new SourceFileObjectInitializer().buildSourceFileObject(sourceFilePath, resolutionVersion, modifiedLines);
                                        int lineNumber_int = Integer.parseInt(lineNumber);
                                        LinkedHashMap<String, String> methodSignatureAndClassName = lineNumberAndMethodSignatureAndClassName.get(lineNumber_int);

                                        if (methodSignatureAndClassName != null) {

                                            className = methodSignatureAndClassName.get("className");
                                            methodSignature = methodSignatureAndClassName.get("methodSignature");

                                            if (methodSignature != null && !methodSignature.equals("no_method_signature")) {
                                                methodSignatures.add(methodSignature);
                                            }
                                        }
                                    }
                                }
                                if (className != null && !className.equals("no_class_name")) {
                                    vulnerableComponents.put(className, methodSignatures);
                                }

                                vulnerableComponentsObjects.put("vulnerableComponents", vulnerableComponents);
                                filePathSuffixesObjects.put(filePathSuffix, vulnerableComponentsObjects);
                            }
                            commitUrlObjects.put(commitUrl, filePathSuffixesObjects);
                        }
                        commitObjects.put(commitId, commitUrlObjects);
                    }
                    cveObjects.put(cveId, commitObjects);
                }
                updatedResolutionVersionObjects.put(resolutionVersion, cveObjects);
            }
            jsonArray.put(updatedResolutionVersionObjects);
        }
        // FileWriter fileWriter = new FileWriter("ground_truth_phase_5.json");
        // fileWriter.write(jsonArray.toString());
        // fileWriter.close();
        System.out.println("Phase 5 completed!");
    }
}
