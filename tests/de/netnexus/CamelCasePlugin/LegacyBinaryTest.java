package de.netnexus.CamelCasePlugin;

// Compile against the released JAR and run against the packaged hotfix.
public class LegacyBinaryTest {
    private static final String[] CASES = {
        "kebab-case", "SNAKE_CASE", "CamelCase", "camelCase", "snake_case", "space case", "Camel Case"
    };

    private static String next(String text) {
        return Conversion.transform(text, true, true, true, true, true, true, true, CASES);
    }

    public static void main(String[] args) throws Exception {
        for (String start : new String[]{"RESOLVED", "FieldMode", "field_mode", "field mode"}) {
            String text = start;
            boolean returned = false;
            for (int i = 0; i < 28; i++) {
                String converted = next(text);
                if (converted.equals(text)) throw new AssertionError("Stuck at " + text);
                if (converted.equals(start)) returned = true;
                text = converted;
            }
            if (!returned) throw new AssertionError("Did not cycle back to " + start);
            System.out.println("PASS released binary API cycle: " + start);
        }
        String converted = Conversion.transform("field_mode", false, false, false, true, false, false, false, CASES);
        if (!"FIELD_MODE".equals(converted)) throw new AssertionError(converted);
        var capitalize = Conversion.class.getDeclaredMethod("capitalize", String.class);
        capitalize.setAccessible(true);
        String whitespace = (String) capitalize.invoke(null, " \tfield  mODE\nnext");
        if (!" \tField  MODE\nNext".equals(whitespace)) throw new AssertionError(whitespace);
        System.out.println("PASS packaged legacy API: skipped formats and whitespace");
    }
}
