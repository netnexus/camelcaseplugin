package de.netnexus.CamelCasePlugin;

public class ConversionTest {
    private static void check(String expected, String actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected [" + expected + "], got [" + actual + "]");
        }
    }

    private static void cycle(String... values) {
        String text = values[0];
        String[] cases = Conversion.ConversionList.toArray(new String[0]);
        for (int i = 1; i <= values.length * 2; i++) {
            text = Conversion.transform(text, Conversion.getNext(Conversion.CaseType(text), cases));
            check(values[i % values.length], text);
        }
    }

    public static void main(String[] args) throws Exception {
        cycle("RESOLVED", "Resolved", "resolved");
        cycle("FieldMode", "fieldMode", "field_mode", "field mode", "Field Mode", "field-mode", "FIELD_MODE");
        // Skipping disabled formats still traverses the capitalization step.
        check("FIELD_MODE", Conversion.transform("field_mode", "SNAKE_CASE"));
        check("FieldMode", Conversion.transform("field_mode", "CamelCase"));
        var capitalize = Conversion.class.getDeclaredMethod("capitalize", String.class);
        capitalize.setAccessible(true);
        check("", (String) capitalize.invoke(null, ""));
        check(" \tField  MODE\nNext\r\nWord", (String) capitalize.invoke(null, " \tfield  mODE\nnext\r\nword"));
        check("ǅuro", (String) capitalize.invoke(null, "ǆuro"));
        System.out.println("PASS: repeated single/multi-word cycles, skipped formats, whitespace and title case");
    }
}
