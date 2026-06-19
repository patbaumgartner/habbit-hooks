package com.patbaumgartner.habithooks.scope;

import com.patbaumgartner.habithooks.baseline.GitBridge;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FileScopeTest {

    @TempDir
    Path tempDir;

    @Test
    void allFilesExcludesTestSourcesByDefault() throws IOException {
        writeJava("src/main/java/com/example/Main.java");
        writeJava("src/test/java/com/example/MainTest.java");

        List<Path> files = new FileScope(tempDir).allFiles(true);

        assertThat(files).hasSize(1);
        assertThat(files.get(0).toString()).endsWith("Main.java");
    }

    @Test
    void allFilesIncludesTestSourcesWhenRequested() throws IOException {
        writeJava("src/main/java/com/example/Main.java");
        writeJava("src/test/java/com/example/MainTest.java");

        List<Path> files = new FileScope(tempDir).allFiles(false);

        assertThat(files).hasSize(2);
    }

    @Test
    void allFilesAlwaysExcludesTargetDirectory() throws IOException {
        writeJava("src/main/java/com/example/Main.java");
        writeJava("target/classes/com/example/Generated.java");

        List<Path> files = new FileScope(tempDir).allFiles(false);

        assertThat(files).hasSize(1);
        assertThat(files.get(0).toString()).endsWith("Main.java");
    }

    @Test
    void allFilesAlwaysExcludesBuildDirectory() throws IOException {
        writeJava("src/main/java/com/example/Main.java");
        writeJava("build/generated/aotSources/com/example/BeanDefinitions.java");

        List<Path> files = new FileScope(tempDir).allFiles(false);

        assertThat(files).hasSize(1);
        assertThat(files.get(0).toString()).endsWith("Main.java");
    }

    @Test
    void changedSinceBranchResolvesExistingJavaFiles() throws IOException {
        writeJava("src/main/java/com/example/Changed.java");
        GitBridge git = mock(GitBridge.class);
        when(git.changedFilesSinceBranch("main")).thenReturn("src/main/java/com/example/Changed.java\nREADME.md\n");

        List<Path> files = new FileScope(tempDir, git).changedSinceBranch("main");

        assertThat(files).hasSize(1);
        assertThat(files.get(0).toString()).endsWith("Changed.java");
    }

    @Test
    void changedInLastNReturnsEmptyForBlankOutput() {
        GitBridge git = mock(GitBridge.class);
        when(git.changedFilesInLastN(3)).thenReturn("");

        assertThat(new FileScope(tempDir, git).changedInLastN(3)).isEmpty();
    }

    @Test
    void changedSinceCommitResolvesExistingJavaFiles() throws IOException {
        writeJava("src/main/java/com/example/Touched.java");
        GitBridge git = mock(GitBridge.class);
        when(git.changedFilesSinceCommit("abc123")).thenReturn("src/main/java/com/example/Touched.java\n");

        List<Path> files = new FileScope(tempDir, git).changedSinceCommit("abc123");

        assertThat(files).hasSize(1);
        assertThat(files.get(0).toString()).endsWith("Touched.java");
    }

    private void writeJava(String relativePath) throws IOException {
        Path file = tempDir.resolve(relativePath);
        Files.createDirectories(file.getParent());
        Files.writeString(file, "public class Placeholder {}");
    }

}
