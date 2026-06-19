package com.patbaumgartner.habithooks.baseline;

import com.patbaumgartner.habithooks.model.Violation;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaselineManagerTest {

    @TempDir
    Path tempDir;

    private static final Violation VIOLATION = new Violation("checkstyle:MethodLength", "Foo.java", 5, "Too long");

    @Test
    void filterReturnsAllViolationsWhenNoBaselineExists() {
        BaselineManager manager = new BaselineManager(tempDir, mock(GitBridge.class));
        assertThat(manager.filter(List.of(VIOLATION))).containsExactly(VIOLATION);
    }

    @Test
    void snoozeThenFilterSuppressesWhenHashMatchesAndClean() {
        GitBridge git = mock(GitBridge.class);
        when(git.lastCommitHash("Foo.java")).thenReturn(Optional.of("abc123"));
        when(git.isDirty("Foo.java")).thenReturn(false);
        BaselineManager manager = new BaselineManager(tempDir, git);

        manager.snooze(List.of(VIOLATION));

        assertThat(manager.filter(List.of(VIOLATION))).isEmpty();
    }

    @Test
    void filterKeepsViolationWhenFileIsDirty() {
        GitBridge git = mock(GitBridge.class);
        when(git.lastCommitHash("Foo.java")).thenReturn(Optional.of("abc123"));
        when(git.isDirty("Foo.java")).thenReturn(true);
        BaselineManager manager = new BaselineManager(tempDir, git);

        manager.snooze(List.of(VIOLATION));

        assertThat(manager.filter(List.of(VIOLATION))).containsExactly(VIOLATION);
    }

    @Test
    void filterKeepsViolationWhenCommitHashChanged() {
        GitBridge git = mock(GitBridge.class);
        when(git.lastCommitHash("Foo.java")).thenReturn(Optional.of("abc123")).thenReturn(Optional.of("def456"));
        when(git.isDirty("Foo.java")).thenReturn(false);
        BaselineManager manager = new BaselineManager(tempDir, git);

        manager.snooze(List.of(VIOLATION));

        assertThat(manager.filter(List.of(VIOLATION))).containsExactly(VIOLATION);
    }

    @Test
    void statusReportsFileAndViolationCounts() {
        GitBridge git = mock(GitBridge.class);
        when(git.lastCommitHash("Foo.java")).thenReturn(Optional.of("abc123"));
        BaselineManager manager = new BaselineManager(tempDir, git);

        manager.snooze(List.of(VIOLATION));

        assertThat(manager.status()).isEqualTo("Baseline: 1 file(s), 1 snoozed violation(s).");
    }

    @Test
    void statusReportsNoBaselineWhenFileMissing() {
        BaselineManager manager = new BaselineManager(tempDir, mock(GitBridge.class));
        assertThat(manager.status()).isEqualTo("No baseline file found.");
    }

    @Test
    void pruneRemovesEntriesForDeletedFiles() throws IOException {
        GitBridge git = mock(GitBridge.class);
        when(git.lastCommitHash("Ghost.java")).thenReturn(Optional.of("abc123"));
        BaselineManager manager = new BaselineManager(tempDir, git);
        manager.snooze(List.of(new Violation("pmd:GodClass", "Ghost.java", 1, "God class")));

        manager.prune();

        assertThat(manager.status()).isEqualTo("Baseline: 0 file(s), 0 snoozed violation(s).");
        assertThat(Files.exists(tempDir.resolve(".habit-hooks-baseline.json"))).isTrue();
    }

}
