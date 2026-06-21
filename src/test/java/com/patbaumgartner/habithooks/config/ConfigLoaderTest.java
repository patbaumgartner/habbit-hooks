package com.patbaumgartner.habithooks.config;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

class ConfigLoaderTest {

    @TempDir
    Path tempDir;

    @Test
    void returnsDefaultConfigWhenNoFileExists() {
        HabitHooksConfig config = ConfigLoader.load(null, tempDir);
        assertThat(config).isNotNull();
        assertThat(config.getScope().getBranchBase()).isEqualTo("main");
        assertThat(config.getScope().isOnlyChangedFiles()).isTrue();
    }

    @Test
    void loadsConfigFromFile() throws Exception {
        Path configFile = tempDir.resolve(".habit-hooks.yaml");
        Files.writeString(configFile, """
                prompts: ./custom-prompts
                scope:
                  onlyChangedFiles: false
                  branchBase: develop
                """);
        HabitHooksConfig config = ConfigLoader.load(null, tempDir);
        assertThat(config.getPromptsDir()).isEqualTo("./custom-prompts");
        assertThat(config.getScope().getBranchBase()).isEqualTo("develop");
        assertThat(config.getScope().isOnlyChangedFiles()).isFalse();
    }

    @Test
    void loadsConfigFromExplicitPath() throws Exception {
        Path configFile = tempDir.resolve("custom.yaml");
        Files.writeString(configFile, """
                prompts: ./prompts-custom
                """);
        HabitHooksConfig config = ConfigLoader.load(configFile.toString(), tempDir);
        assertThat(config.getPromptsDir()).isEqualTo("./prompts-custom");
    }

    @Test
    void resolvesExplicitRelativeConfigPathFromWorkingDir() throws Exception {
        Path configFile = tempDir.resolve("config/custom.yaml");
        Files.createDirectories(configFile.getParent());
        Files.writeString(configFile, """
                scope:
                  branchBase: develop
                """);

        HabitHooksConfig config = ConfigLoader.load("config/custom.yaml", tempDir);

        assertThat(config.getScope().getBranchBase()).isEqualTo("develop");
    }

    @Test
    void returnsDefaultConfigOnMalformedYaml() throws Exception {
        Path configFile = tempDir.resolve(".habit-hooks.yaml");
        Files.writeString(configFile, "{ invalid yaml: [");
        HabitHooksConfig config = ConfigLoader.load(null, tempDir);
        assertThat(config).isNotNull();
        assertThat(config.getScope().getBranchBase()).isEqualTo("main");
    }

    @Test
    void fallsBackToDefaultsWhenConfigContainsNullOrBlankValues() throws Exception {
        Path configFile = tempDir.resolve(".habit-hooks.yaml");
        Files.writeString(configFile, """
                prompts:
                rules:
                scope:
                    branchBase: ""
                analyzers:
                    checkstyle:
                        configFile:
                    pmd:
                        rulesets:
                """);

        HabitHooksConfig config = ConfigLoader.load(null, tempDir);

        assertThat(config.getPromptsDir()).isEqualTo("./prompts");
        assertThat(config.getRules()).isEmpty();
        assertThat(config.getScope().getBranchBase()).isEqualTo("main");
        assertThat(config.getAnalyzers()).containsKeys("checkstyle", "pmd");
        assertThat(config.getAnalyzers().get("checkstyle").getConfigFile()).isEqualTo("checkstyle.xml");
        assertThat(config.getAnalyzers().get("pmd").getRulesets()).containsExactly("pmd-ruleset.xml");
    }

    @Test
    void loadsAndDefaultsTaikaiTestClass() throws Exception {
        Path configFile = tempDir.resolve(".habit-hooks.yaml");
        Files.writeString(configFile, """
                analyzers:
                  taikai:
                    enabled: true
                """);
        HabitHooksConfig config = ConfigLoader.load(null, tempDir);
        assertThat(config.getAnalyzers()).containsKey("taikai");
        assertThat(config.getAnalyzers().get("taikai").isEnabled()).isTrue();
        assertThat(config.getAnalyzers().get("taikai").getTestClass()).isEqualTo("ArchitectureTest");
    }

    @Test
    void loadsTaikaiCustomTestClass() throws Exception {
        Path configFile = tempDir.resolve(".habit-hooks.yaml");
        Files.writeString(configFile, """
                analyzers:
                  taikai:
                    enabled: true
                    testClass: MyArchTest
                """);
        HabitHooksConfig config = ConfigLoader.load(null, tempDir);
        assertThat(config.getAnalyzers().get("taikai").getTestClass()).isEqualTo("MyArchTest");
    }

    @Test
    void loadsMavenBackedAnalyzerOverrides() throws Exception {
        Path configFile = tempDir.resolve(".habit-hooks.yaml");
        Files.writeString(configFile, """
                analyzers:
                  spotbugs:
                    enabled: true
                    goal: verify spotbugs:spotbugs
                    reportFile: build/spotbugs.xml
                """);

        HabitHooksConfig config = ConfigLoader.load(null, tempDir);
        AnalyzerConfig spotbugs = config.getAnalyzers().get("spotbugs");

        assertThat(spotbugs.isEnabled()).isTrue();
        assertThat(spotbugs.getGoal()).isEqualTo("verify spotbugs:spotbugs");
        assertThat(spotbugs.getReportFile()).isEqualTo("build/spotbugs.xml");
    }

}
