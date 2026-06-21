package com.patbaumgartner.habithooks.analyzer;

import com.patbaumgartner.habithooks.model.Violation;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/** Parses PIT mutation XML reports. */
final class PitestReportParser {

    private PitestReportParser() {
    }

    static List<Violation> parse(Path reportPath, Path workingDir, String toolPrefix)
            throws ParserConfigurationException, IOException, SAXException {
        NodeList mutations = ReportSupport.parseXml(reportPath).getElementsByTagName("mutation");
        List<Violation> violations = new ArrayList<>();
        for (int index = 0; index < mutations.getLength(); index++) {
            addSurvivingMutation(violations, (Element) mutations.item(index), toolPrefix);
        }
        return List.copyOf(violations);
    }

    private static void addSurvivingMutation(List<Violation> violations, Element mutation, String toolPrefix) {
        String status = mutation.getAttribute("status");
        if (!"SURVIVED".equals(status) && !"NO_COVERAGE".equals(status)) {
            return;
        }
        String sourceFile = ReportSupport.textOfFirst(mutation, "sourceFile")
            .orElse("target/pit-reports/mutations.xml");
        int line = ReportSupport.textOfFirst(mutation, "lineNumber").map(ReportSupport::parseInt).orElse(1);
        String mutator = ReportSupport.textOfFirst(mutation, "mutator").orElse("mutation");
        String description = ReportSupport.textOfFirst(mutation, "description")
            .orElse("Mutation was not killed by tests.");
        violations.add(new Violation(toolPrefix + ":" + status, sourceFile, line, mutator + ": " + description));
    }

}
