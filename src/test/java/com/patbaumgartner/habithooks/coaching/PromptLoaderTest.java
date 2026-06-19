package com.patbaumgartner.habithooks.coaching;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PromptLoaderTest {

    @Test
    void toFilenameReplacesColonWithDash() {
        assertThat(PromptLoader.toFilename("checkstyle:MethodLength")).isEqualTo("checkstyle-MethodLength.md");
    }

    @Test
    void toFilenameHandlesPmdRuleId() {
        assertThat(PromptLoader.toFilename("pmd:GodClass")).isEqualTo("pmd-GodClass.md");
    }

    @Test
    void toFilenameStripsAtSign() {
        assertThat(PromptLoader.toFilename("@plugin/some:rule")).isEqualTo("plugin-some-rule.md");
    }

    @Test
    void loadsBuiltInCheckstyleMethodLengthPrompt() {
        PromptLoader loader = new PromptLoader(Path.of("/nonexistent"));
        Optional<String> prompt = loader.load("checkstyle:MethodLength");
        assertThat(prompt).isPresent();
        assertThat(prompt.get()).contains("responsibilities");
    }

    @Test
    void loadsBuiltInPmdGodClassPrompt() {
        PromptLoader loader = new PromptLoader(Path.of("/nonexistent"));
        Optional<String> prompt = loader.load("pmd:GodClass");
        assertThat(prompt).isPresent();
        assertThat(prompt.get()).contains("Single Responsibility");
    }

    @Test
    void returnsEmptyForUnknownRule() {
        PromptLoader loader = new PromptLoader(Path.of("/nonexistent"));
        assertThat(loader.load("custom:unknown:rule")).isEmpty();
    }

    @Test
    void allCoachingPromptFilesAreLoadable() {
        // Regression guard: if a prompt file is renamed or deleted this test fails.
        // To add a new coached rule: create the prompt file and add the rule ID here.
        List<String> coachedRules = List.of("checkstyle:MethodLength", "checkstyle:ParameterNumber",
                "checkstyle:CyclomaticComplexity", "checkstyle:VisibilityModifier", "checkstyle:MagicNumber",
                "checkstyle:NestedIfDepth", "checkstyle:NestedTryDepth", "checkstyle:BooleanExpressionComplexity",
                "pmd:NcssCount", "pmd:GodClass", "pmd:UnusedPrivateField", "pmd:UnusedLocalVariable",
                "pmd:EmptyCatchBlock", "pmd:LiteralsFirstInComparisons", "pmd:ReturnEmptyCollectionRatherThanNull",
                "pmd:UseCollectionIsEmpty", "pmd:UseEqualsToCompareStrings", "pmd:OverrideBothEqualsAndHashcode",
                "pmd:AvoidReassigningParameters", "pmd:LooseCoupling", "pmd:ArrayIsStoredDirectly",
                "pmd:PreserveStackTrace");
        PromptLoader loader = new PromptLoader(Path.of("/nonexistent"));
        List<String> missing = new ArrayList<>();
        for (String ruleId : coachedRules) {
            if (loader.load(ruleId).isEmpty()) {
                missing.add(ruleId);
            }
        }
        assertThat(missing)
            .as("Coached rules whose prompt file could not be loaded — did you rename or delete a prompt file?")
            .isEmpty();
    }

}
