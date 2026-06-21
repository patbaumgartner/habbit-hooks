package com.patbaumgartner.habithooks.analyzer;

import com.patbaumgartner.habithooks.model.Violation;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class JSpecifyAnalyzerTest {

    @TempDir
    Path tempDir;

    @Test
    void isAvailableWhenPomExists() throws Exception {
        Files.writeString(tempDir.resolve("pom.xml"), "<project/>");

        assertThat(new JSpecifyAnalyzer().isAvailable(tempDir)).isTrue();
    }

    @Test
    void reportsMissingDependencyWhenEnabledWithoutJSpecify() throws Exception {
        Files.writeString(tempDir.resolve("pom.xml"), "<project/>");

        List<Violation> violations = new JSpecifyAnalyzer().analyze(List.of(), tempDir);

        assertThat(violations).extracting(Violation::ruleId).containsExactly("jspecify:DependencyMissing");
    }

    @Test
    void reportsNotAdoptedWhenDependencyIsUnused() throws Exception {
        writePomWithJSpecify();
        writeSource("package com.example; class Example {}");

        List<Violation> violations = new JSpecifyAnalyzer().analyze(List.of(), tempDir);

        assertThat(violations).extracting(Violation::ruleId).containsExactly("jspecify:NotAdopted");
    }

    @Test
    void returnsCleanWhenAnnotationsAreUsed() throws Exception {
        writePomWithJSpecify();
        writeSource("""
                package com.example;
                import org.jspecify.annotations.NullMarked;
                @NullMarked class Example {}
                """);

        assertThat(new JSpecifyAnalyzer().analyze(List.of(), tempDir)).isEmpty();
    }

    private void writePomWithJSpecify() throws Exception {
        Files.writeString(tempDir.resolve("pom.xml"), """
                <project>
                  <dependencies>
                    <dependency>
                      <groupId>org.jspecify</groupId>
                      <artifactId>jspecify</artifactId>
                    </dependency>
                  </dependencies>
                </project>
                """);
    }

    private void writeSource(String source) throws Exception {
        Path sourceFile = tempDir.resolve("src/main/java/com/example/Example.java");
        Files.createDirectories(sourceFile.getParent());
        Files.writeString(sourceFile, source);
    }

}
