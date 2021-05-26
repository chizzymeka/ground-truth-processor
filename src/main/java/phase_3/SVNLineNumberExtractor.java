// PHASE 3

package phase_3;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utilities.ProgramHalter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class SVNLineNumberExtractor {

    public void getSVNLineNumber() throws IOException, InterruptedException {

        String svnUrlPrefix = "https://svn.apache.org/";
        String groundTruthDataPhase2path = "ground_truth_phase_2.json";
        String groundTruthDataPhase2Data = new String((Files.readAllBytes(Paths.get(groundTruthDataPhase2path))));
        LinkedHashMap<String, String> modificationLocationDetails = new LinkedHashMap<>();
        JSONArray resolutionVersionObjects = new JSONArray(groundTruthDataPhase2Data);

        for (int i = 0; i < resolutionVersionObjects.length(); i++) {

            if ((i > 0) && (i % 10 == 0)) {
                new ProgramHalter().haltProgram(60);
            }

            JSONObject resolutionVersionObject = resolutionVersionObjects.getJSONObject(i);
            Set<JSONObject> resolutionVersionObjectKeys = resolutionVersionObject.keySet();

            for (Object resolutionVersionObjectKey : resolutionVersionObjectKeys) {

                String resolutionVersion = (String) resolutionVersionObjectKey;
                System.out.println(i + 1 + ": " + resolutionVersion);
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
                                // ----------------------------------------------------------------------------------------------------
                                // jsoup code
                                // ----------------------------------------------------------------------------------------------------

                                if (commitUrl.contains("https://svn.apache.org/")) {

                                    // Connect to Tomcat website.
                                    Response response = Jsoup.connect(commitUrl).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0").referrer("https://www.google.co.uk/").timeout(15_000).followRedirects(true).execute();
                                    Document tomcat7SVNRevisionWebpage = response.parse();

                                    Elements javaSourceFileHyperlinks = tomcat7SVNRevisionWebpage.select("a[href*=\".java\"][title~=View File Contents]");

                                    for (Element javaSourceFileHyperlink : javaSourceFileHyperlinks) {

                                        String svnUrlSuffix = javaSourceFileHyperlink.attr("href");
                                        String revisionDetailsUrl = svnUrlPrefix + svnUrlSuffix;
                                        String replacementText = "annotate=" + commitId;
                                        revisionDetailsUrl = revisionDetailsUrl.replace("view=markup", replacementText);

                                        if (revisionDetailsUrl.contains(filePathSuffix)) {

                                            Response response_2 = Jsoup.connect(revisionDetailsUrl).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0").referrer("https://www.google.co.uk/").timeout(15_000).followRedirects(true).execute();
                                            Document revisionDetailsWebpage = response_2.parse();

                                            // Every SVN source file revision webpage features a "vc_row_special" class
                                            // (yellow in colour) which shows where the file was modified. So, we
                                            // target those rows and extract their line numbers.
                                            List<Element> modifiedRows = revisionDetailsWebpage.getElementsByClass("vc_row_special");
                                            LinkedHashMap<String, LinkedHashMap<String, String>> lines = new LinkedHashMap<>();

                                            for (Element modifiedRow : modifiedRows) {

                                                String lineNumber = modifiedRow.id();
                                                lineNumber = lineNumber.replace("l", "");
                                                modificationLocationDetails.put("className", "");
                                                modificationLocationDetails.put("methodSignature", "");

                                                // We put the data in a LinkedHashMap first before inserting it into the
                                                // JSON object so that the line number orders will be preserved.
                                                lines.put(lineNumber, modificationLocationDetails);

                                                for (Entry<String, LinkedHashMap<String, String>> entry : lines.entrySet()) {
                                                    String line = entry.getKey();
                                                    LinkedHashMap<String, String> locationDetails = entry.getValue();
                                                    // Update the JSON data with the line numbers for SVN modifications.
                                                    lineNumberObject.put(line, locationDetails);
                                                }
                                            }
                                        }
                                    }
                                }
                                // ----------------------------------------------------------------------------------------------------
                            }
                        }
                    }
                }
            }
        }
        // FileWriter fileWriter = new FileWriter("ground_truth_phase_3.json");
        // fileWriter.write(resolutionVersionObjects.toString());
        // fileWriter.close();
        System.out.println("Phase 3 completed!");
    }
}
