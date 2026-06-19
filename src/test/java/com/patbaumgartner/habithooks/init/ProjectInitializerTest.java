package com.patbaumgartner.habithooks.init;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectInitializerTest {

    @TempDir
    Path tempDir;

    @Test
    void dryRunDoesNotWriteAnyFiles() {
        Output output = run(true, false);

        assertThat(output.text()).contains("[dry-run]");
        assertThat(Files.exists(tempDir.resolve(".habit-hooks.yaml"))).isFalse();
        assertThat(Files.exists(tempDir.resolve("checkstyle.xml"))).isFalse();
    }

    @Test
    void writesScaffoldFilesWhenAbsent() {
        Output output = run(false, false);

        assertThat(output.text()).contains("initialized");
        assertThat(Files.exists(tempDir.resolve("checkstyle.xml"))).isTrue();
        assertThat(Files.exists(tempDir.resolve("pmd-ruleset.xml"))).isTrue();
        assertThat(Files.exists(tempDir.resolve(".habit-hooks.yaml"))).isTrue();
        assertThat(Files.exists(tempDir.resolve(".habit-hooks-baseline.json"))).isTrue();
    }

    @Test
    void skipsFilesThatAlreadyExist() throws IOException {
        Files.writeString(tempDir.resolve("checkstyle.xml"), "<module/>");

        Output output = run(false, false);

        assertThat(output.text()).contains("skip  checkstyle.xml (already exists)");
        assertThat(Files.readString(tempDir.resolve("checkstyle.xml"))).isEqualTo("<module/>");
    }

    @Test
    void taikaiScaffoldsArchitectureTestWhenTestDirExists() throws IOException {
        Files.createDirectories(tempDir.resolve("src/test/java"));

        run(false, true);

        assertThat(Files.exists(tempDir.resolve("src/test/java/ArchitectureTest.java"))).isTrue();
    }

    @Test
    void taikaiIsSkippedWhenNoTestDirectory() {
        Output output = run(false, true);

        assertThat(output.text()).contains("no src/test/java found");
        assertThat(Files.exists(tempDir.resolve("src/test/java/ArchitectureTest.java"))).isFalse();
    }

    private Output run(boolean dryRun, boolean taikai) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        new ProjectInitializer(tempDir, dryRun, taikai, out).initialize();
        return new Output(buffer.toString(StandardCharsets.UTF_8));
    }

    private record Output(String text) {
    }

}
