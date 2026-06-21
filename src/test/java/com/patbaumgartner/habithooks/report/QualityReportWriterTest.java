package com.patbaumgartner.habithooks.report;

import com.patbaumgartner.habithooks.model.AnalysisResult;
import com.patbaumgartner.habithooks.model.Violation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class QualityReportWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void writesMarkdownReport() throws Exception {
        AnalysisResult result = new AnalysisResult(List.of(new Violation("owasp:CveHigh", "pom.xml", -1, "CVE")), 3);
        QualityReport report = new QualityReportBuilder().build(result, true);

        Path output = new QualityReportWriter().write(report, tempDir, "markdown");

        assertThat(output).hasFileName("report.markdown");
        assertThat(Files.readString(output)).contains("habit-hooks local quality report", "owasp:CveHigh");
    }

    @Test
    void writesJsonHtmlAndSarifReports() throws Exception {
        AnalysisResult result = new AnalysisResult(List.of(new Violation("pmd:GodClass", "Big.java", 7, "Too big")), 1);
        QualityReport report = new QualityReportBuilder().build(result, true);

        Path json = new QualityReportWriter().write(report, tempDir, "json");
        Path html = new QualityReportWriter().write(report, tempDir, "html");
        Path sarif = new QualityReportWriter().write(report, tempDir, "sarif");

        assertThat(Files.readString(json)).contains("pmd:GodClass");
        assertThat(Files.readString(html)).contains("habit-hooks local quality report");
        assertThat(Files.readString(sarif)).contains("\"version\"", "Big.java");
    }

}
