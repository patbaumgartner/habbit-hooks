package com.patbaumgartner.habithooks.baseline;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BaselineDocumentTest {

    @Test
    void defaultsToVersionOneWithNoEntries() {
        BaselineDocument doc = new BaselineDocument();
        assertThat(doc.getVersion()).isEqualTo(1);
        assertThat(doc.getEntries()).isEmpty();
    }

    @Test
    void settersRoundTrip() {
        BaselineDocument doc = new BaselineDocument();
        doc.setVersion(2);
        BaselineDocument.BaselineEntry entry = new BaselineDocument.BaselineEntry();
        entry.setCommitHash("abc123");
        entry.setRuleIds(List.of("checkstyle:MethodLength"));
        doc.setEntries(Map.of("Foo.java", entry));

        assertThat(doc.getVersion()).isEqualTo(2);
        assertThat(doc.getEntries()).containsKey("Foo.java");
        assertThat(doc.getEntries().get("Foo.java").getCommitHash()).isEqualTo("abc123");
        assertThat(doc.getEntries().get("Foo.java").getRuleIds()).containsExactly("checkstyle:MethodLength");
    }

    @Test
    void entryRuleIdsAreStoredAsDefensiveCopy() {
        BaselineDocument.BaselineEntry entry = new BaselineDocument.BaselineEntry();
        List<String> source = new ArrayList<>(List.of("pmd:GodClass"));
        entry.setRuleIds(source);
        source.clear();
        assertThat(entry.getRuleIds()).containsExactly("pmd:GodClass");
    }

}
