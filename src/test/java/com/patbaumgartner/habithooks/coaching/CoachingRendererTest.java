package com.patbaumgartner.habithooks.coaching;

import com.patbaumgartner.habithooks.model.CoachingGroup;
import com.patbaumgartner.habithooks.model.Violation;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CoachingRendererTest {

    @Test
    void rendersPassWhenNoViolations() {
        String output = render(List.of());
        assertThat(output).contains("✅ Habit Hooks: all checks passed.");
    }

    @Test
    void rendersFailHeaderWithViolationCount() {
        CoachingGroup group = new CoachingGroup("checkstyle:MethodLength", "Oversized Method",
                Optional.of("Methods over 25 lines…"),
                List.of(new Violation("checkstyle:MethodLength", "Foo.java", 5, "Too long")));
        String output = render(List.of(group));
        assertThat(output).contains("❌ Habit Hooks: 1 violation");
    }

    @Test
    void rendersCoachedGroupWithPrompt() {
        CoachingGroup group = new CoachingGroup("checkstyle:MethodLength", "Oversized Method",
                Optional.of("Coaching text here."),
                List.of(new Violation("checkstyle:MethodLength", "Bar.java", 10, "Too long")));
        String output = render(List.of(group));
        assertThat(output).contains("❌ Oversized Method")
            .contains("Coaching text here.")
            .contains("Violations:")
            .contains("Bar.java:10");
    }

    @Test
    void rendersUncoachedSectionForRulesWithNoPrompt() {
        CoachingGroup uncoached = new CoachingGroup("custom:WeirdRule", "custom:WeirdRule", Optional.empty(),
                List.of(new Violation("custom:WeirdRule", "Baz.java", 3, "weird")));
        String output = render(List.of(uncoached));
        assertThat(output).contains("⚠️  Uncoached rules")
            .contains("custom:WeirdRule (1 violation)")
            .contains("Baz.java:3");
    }

    @Test
    void uncoachedSectionCapsAtTenAndShowsMoreLine() {
        List<Violation> violations = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            violations.add(new Violation("checkstyle:FileTabCharacter", "File" + i + ".java", i, "tab"));
        }
        CoachingGroup uncoached = new CoachingGroup("checkstyle:FileTabCharacter", "checkstyle:FileTabCharacter",
                Optional.empty(), violations);
        String output = render(List.of(uncoached));
        assertThat(output).contains("checkstyle:FileTabCharacter (15 violations)")
            .contains("File10.java:10")
            .doesNotContain("File11.java")
            .contains("… and 5 more");
    }

    @Test
    void rendersTaikaiHintOnCleanRun() {
        String output = render(List.of());
        assertThat(output).contains("Taikai");
    }

    private static String render(List<CoachingGroup> groups) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new CoachingRenderer(new PrintStream(baos, true, StandardCharsets.UTF_8)).render(groups);
        return baos.toString(StandardCharsets.UTF_8);
    }

}
