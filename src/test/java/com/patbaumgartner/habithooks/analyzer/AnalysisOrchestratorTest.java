package com.patbaumgartner.habithooks.analyzer;

import com.patbaumgartner.habithooks.model.AnalysisResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class AnalysisOrchestratorTest {

    @TempDir
    Path tempDir;

    @Test
    void emptyAnalyzerListProducesCleanResult() {
        AnalysisOrchestrator orchestrator = new AnalysisOrchestrator(List.of());
        AnalysisResult result = orchestrator.analyze(List.of(tempDir.resolve("A.java")), tempDir);
        assertThat(result.isClean()).isTrue();
        assertThat(result.filesChecked()).isEqualTo(1);
    }

    @Test
    void skipsUnavailableAnalyzer() {
        AnalysisOrchestrator orchestrator = new AnalysisOrchestrator(List.of(new CheckstyleAnalyzer("checkstyle.xml")));
        AnalysisResult result = orchestrator.analyze(List.of(), tempDir);
        assertThat(result.isClean()).isTrue();
        assertThat(result.filesChecked()).isZero();
    }

    @Test
    void runsAvailableAnalyzerAndMergesViolations() throws IOException {
        Files.writeString(tempDir.resolve("checkstyle.xml"), methodLengthConfig());
        Path javaFile = tempDir.resolve("BigMethod.java");
        Files.writeString(javaFile, longMethodSource());

        AnalysisOrchestrator orchestrator = new AnalysisOrchestrator(List.of(new CheckstyleAnalyzer("checkstyle.xml")));
        AnalysisResult result = orchestrator.analyze(List.of(javaFile), tempDir);

        assertThat(result.isClean()).isFalse();
        assertThat(result.filesChecked()).isEqualTo(1);
        assertThat(result.violations().get(0).ruleId()).isEqualTo("checkstyle:MethodLength");
    }

    private static String methodLengthConfig() {
        return """
                <?xml version="1.0"?>
                <!DOCTYPE module PUBLIC
                    "-//Checkstyle//DTD Checkstyle Configuration 1.3//EN"
                    "https://checkstyle.org/dtds/configuration_1_3.dtd">
                <module name="Checker">
                    <property name="severity" value="warning"/>
                    <module name="TreeWalker">
                        <module name="MethodLength">
                            <property name="max" value="2"/>
                        </module>
                    </module>
                </module>
                """;
    }

    private static String longMethodSource() {
        return """
                public class BigMethod {
                    public void big() {
                        int a = 1;
                        int b = 2;
                        int c = 3;
                        int d = 4;
                    }
                }
                """;
    }

}
