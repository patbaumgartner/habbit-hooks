package com.patbaumgartner.habithooks.coaching;

import java.util.Map;

/**
 * Maps rule IDs to their short human-readable titles shown in coaching output.
 *
 * <p>
 * If a rule has no entry here, the rule ID itself is used as the title.
 */
public final class RuleTitles {

    /**
     * Known rule titles keyed by rule ID (e.g. {@code "checkstyle:MethodLength"}).
     */
    public static final Map<String, String> TITLES = Map.ofEntries(
            // Checkstyle
            Map.entry("checkstyle:MethodLength", "Oversized Method"),
            Map.entry("checkstyle:ParameterNumber", "Too Many Parameters"),
            Map.entry("checkstyle:CyclomaticComplexity", "High Cyclomatic Complexity"),
            Map.entry("checkstyle:JavaNCSS", "High Non-Commenting Source Lines"),
            Map.entry("checkstyle:VisibilityModifier", "Weak Encapsulation"),
            Map.entry("checkstyle:MagicNumber", "Magic Number"),
            Map.entry("checkstyle:NestedIfDepth", "Deeply Nested Conditions"),
            Map.entry("checkstyle:NestedTryDepth", "Deeply Nested Try Blocks"),
            Map.entry("checkstyle:BooleanExpressionComplexity", "Complex Boolean Expression"),
            // PMD — size / complexity
            Map.entry("pmd:NcssCount", "Oversized Method or Class"),
            Map.entry("pmd:ExcessiveParameterList", "Too Many Parameters"),
            Map.entry("pmd:CyclomaticComplexity", "High Cyclomatic Complexity"), Map.entry("pmd:GodClass", "God Class"),
            Map.entry("pmd:TooManyFields", "Too Many Fields"), Map.entry("pmd:TooManyMethods", "Too Many Methods"),
            Map.entry("pmd:CollapsibleIfStatements", "Collapsible If Statements"),
            Map.entry("pmd:SimplifiedTernary", "Simplifiable Ternary"),
            Map.entry("pmd:SingularField", "Singular Field"),
            // PMD — correctness
            Map.entry("pmd:UseEqualsToCompareStrings", "String Compared with =="),
            Map.entry("pmd:OverrideBothEqualsAndHashcode", "Equals Without hashCode"),
            Map.entry("pmd:EmptyCatchBlock", "Empty Catch Block"),
            Map.entry("pmd:PreserveStackTrace", "Lost Stack Trace"),
            Map.entry("pmd:LiteralsFirstInComparisons", "Null-Unsafe String Comparison"),
            // PMD — design / encapsulation
            Map.entry("pmd:ReturnEmptyCollectionRatherThanNull", "Null Instead of Empty Collection"),
            Map.entry("pmd:UseCollectionIsEmpty", "Use isEmpty()"),
            Map.entry("pmd:LooseCoupling", "Concrete Type in API"),
            Map.entry("pmd:AvoidReassigningParameters", "Reassigned Parameter"),
            Map.entry("pmd:ArrayIsStoredDirectly", "Array Stored Directly"),
            Map.entry("pmd:MethodReturnsInternalArray", "Internal Array Exposed"),
            // PMD — unused
            Map.entry("pmd:UnusedPrivateField", "Unused Private Field"),
            Map.entry("pmd:UnusedLocalVariable", "Unused Local Variable"),
            // PMD — duplication
            Map.entry("pmd:CopyPaste", "Duplicated Code"));

    private RuleTitles() {
        // utility class
    }

    /**
     * Returns the human-readable title for the given rule ID, falling back to the rule ID
     * itself when no title is registered.
     * @param ruleId the rule ID (e.g. {@code "checkstyle:MethodLength"})
     * @return the title, never {@code null}
     */
    public static String titleFor(String ruleId) {
        return TITLES.getOrDefault(ruleId, ruleId);
    }

}
