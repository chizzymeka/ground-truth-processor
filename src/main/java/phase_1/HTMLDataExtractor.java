// PHASE 1

package phase_1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utilities.ProgramHalter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HTMLDataExtractor {

    private String severity;
    private String description;
    private String cveId;

    public void buildTomcat7VulnerabilitiesJsonData() throws IOException, InterruptedException {
        JSONArray jsonArray = new JSONArray();
        // Connect to Tomcat website.
        Response response_1 = Jsoup.connect("http://tomcat.apache.org/security-7.html").ignoreContentType(true).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0").referrer("https://www.google.co.uk/").timeout(15_000).followRedirects(true).execute();
        Document tomcat7vulnerabilitiesWebpage = response_1.parse();

        // Get elements that start with 'Fixed_in_Apache_Tomcat_'.
        Elements resolutionHeadlines = tomcat7vulnerabilitiesWebpage.select("[id^=Fixed_in_Apache_Tomcat_]");
        ArrayList<String> severityLevels = new ArrayList<>();
        severityLevels.add("High:");
        severityLevels.add("Important:");
        severityLevels.add("Moderate:");
        severityLevels.add("Low:");

        int counter = 0;

        for (Element resolutionHeadline : resolutionHeadlines) {

            JSONObject jsonObject_1 = new JSONObject();
            JSONObject jsonObject_2 = new JSONObject();

            // Get resolution version.
            String versionNumber = resolutionHeadline.id().split("Fixed_in_Apache_Tomcat_")[1].trim();
            String resolutionVersion = "apache-tomcat-" + versionNumber + "-src";

            // jsonObject_2.put("resolutionVersion", resolutionVersion);
            System.out.println(resolutionVersion);
            Element vulnerabilityInfoText = resolutionHeadline.nextElementSibling();

            // Get severity, description, and CVE ID.
            List<Element> descriptions = vulnerabilityInfoText.select("p");

            for (Element descriptionElement : descriptions) {

                JSONObject jsonObject_3 = new JSONObject();

                for (String severityLevel : severityLevels) {

                    // Check if the bold text contains a severity level, indicating that it is a description.
                    // Without this check, the code will break on 'Fixed in Apache Tomcat 7.0.104' because there is a bold 'Note:' which is clearly not a description.
                    if (descriptionElement.toString().contains(severityLevel)) {
                        String severityAndDescriptionAndCveId = descriptionElement.text();
                        severity = severityAndDescriptionAndCveId.split(":")[0].trim();
                        String descriptionAndCveId = severityAndDescriptionAndCveId.split(":")[1].trim();
                        int indexForLastOccurrence = descriptionAndCveId.lastIndexOf(" CVE-");
                        description = descriptionAndCveId.substring(0, indexForLastOccurrence);
                        cveId = descriptionAndCveId.substring(indexForLastOccurrence).trim();
                    }

                }
                // Get commit information.
                if (descriptionElement.toString().contains("https://svn.apache.org/") || descriptionElement.toString().contains("https://github.com/apache/tomcat/commit/")) {

                    // Some of the SVN revision links have 'revision ' as part of the link. The for-loop below cleans it up and extracts only the revision number.
                    List<String> commitIds = descriptionElement.select("a").eachText();
                    ArrayList<String> commitIdsCopy = new ArrayList<>();

                    for (String commitId : commitIds) {

                        if (commitId.startsWith("revision ")) {
                            commitId = commitId.split("revision ")[1].trim();
                        }
                        commitIdsCopy.add(commitId);

                    }

                    int length = descriptionElement.select("a").size();
                    List<String> urls = descriptionElement.select("a").eachAttr("abs:href");
                    JSONObject jsonObject_4 = new JSONObject();

                    for (int i = 0; i < length; i++) {

                        String commitId = commitIdsCopy.get(i);
                        String commitUrl = urls.get(i);
                        HashMap<String, String> vulnerabilityLocationDetails = new HashMap<>();
                        HashMap<String, HashMap<String, String>> filePathSuffixes = new HashMap<>();
                        HashMap<String, HashMap<String, HashMap<String, String>>> commitUrlAndFilePathSuffixes = new HashMap<>();
                        HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> commitIdAndCommitUrlAndFilePathSuffixes = new HashMap<>();

                        // The code below connect to either SVN or GitHub to get the HTML code for each commit.
                        Response response_2 = Jsoup.connect(commitUrl).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0").referrer("https://www.google.co.uk/").timeout(15_000).followRedirects(true).execute();
                        new ProgramHalter().haltProgram(10);
                        Document versionControlHtmlContent = response_2.parse();

                        List<String> urlTexts = versionControlHtmlContent.select("a").eachText();

                        for (String urlText : urlTexts) {

                            if (urlText.contains(".java")) {

                                // Remove the tomcat-trunk related part of the urls to ensure that the file path suffixes are accurate for use on the local file system.
                                // For example: 'tomcat/trunk/' <- 'java/org/apache/catalina/Context.java'
                                if (urlText.contains("tomcat/trunk/")) {
                                    urlText = urlText.split("tomcat/trunk/")[1];
                                } else if (urlText.contains("tomcat/tc7.0.x/trunk/")) {
                                    urlText = urlText.split("tomcat/tc7.0.x/trunk/")[1];
                                }

                                // As per the information on the Tomcat 7 Vulnerabilities webpage, Commit ID: '1138776' is a reversion instead of a vulnerability fix so, we exclude it from our JSON data.
                                if (!commitId.equals("1138776")) {
                                    filePathSuffixes.put(urlText, vulnerabilityLocationDetails);
                                    commitUrlAndFilePathSuffixes.put(commitUrl, filePathSuffixes);
                                    commitIdAndCommitUrlAndFilePathSuffixes.put(commitId, commitUrlAndFilePathSuffixes);
                                    jsonObject_4.put(commitId, commitUrlAndFilePathSuffixes);
                                }

                            }
                        }
                    }
                    jsonObject_3.put("severity", severity);
                    jsonObject_3.put("description", description);
                    jsonObject_3.put("vulnerabilityFixLocations", jsonObject_4);
                    jsonObject_2.put(cveId, jsonObject_3);
                    jsonObject_1.put(resolutionVersion, jsonObject_2);
                }
            }
            jsonArray.put(jsonObject_1);
        }
        // FileWriter fileWriter = new FileWriter("ground_truth_phase_1.json");
        // fileWriter.write(jsonArray.toString());
        // fileWriter.close();
        System.out.println("Phase 1 completed!");
    }
}