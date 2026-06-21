package com.patbaumgartner.habithooks.analyzer;

import com.patbaumgartner.habithooks.model.Violation;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/** Parses SpotBugs XML reports. */
final class SpotbugsReportParser {

    private SpotbugsReportParser() {
    }

    static List<Violation> parse(Path reportPath, Path workingDir, String toolPrefix)
            throws ParserConfigurationException, IOException, SAXException {
        NodeList bugs = ReportSupport.parseXml(reportPath).getElementsByTagName("BugInstance");
        List<Violation> violations = new ArrayList<>();
        for (int index = 0; index < bugs.getLength(); index++) {
            violations.add(toViolation((Element) bugs.item(index), workingDir, toolPrefix));
        }
        return List.copyOf(violations);
    }

    private static Violation toViolation(Element bug, Path workingDir, String toolPrefix) {
        String type = valueOrDefault(bug.getAttribute("type"), "BugInstance");
        Location location = sourceLocation(bug).orElse(new Location(workingDir.resolve("spotbugsXml.xml"), 1));
        String message = ReportSupport.textOfFirst(bug, "LongMessage").orElse("SpotBugs reported " + type + ".");
        return new Violation(toolPrefix + ":" + type, ReportSupport.relativize(location.file(), workingDir),
                location.line(), message);
    }

    private static Optional<Location> sourceLocation(Element bug) {
        NodeList locations = bug.getElementsByTagName("SourceLine");
        if (locations.getLength() == 0) {
            return Optional.empty();
        }
        Element location = (Element) locations.item(0);
        String sourcePath = valueOrDefault(location.getAttribute("sourcepath"), location.getAttribute("sourcefile"));
        return Optional.of(new Location(Path.of(sourcePath), ReportSupport.parseInt(location.getAttribute("start"))));
    }

    private static String valueOrDefault(String value, String defaultValue) {
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private record Location(Path file, int line) {
    }

}
