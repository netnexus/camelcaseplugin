package de.netnexus.CamelCasePlugin;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

class Conversion {


    private static final String CONVERSION_SNAKE_CASE = "snake_case";
    private static final String CONVERSION_SPACE_CASE = "space case";
    private static final String CONVERSION_KEBAB_CASE = "kebab-case";
    private static final String CONVERSION_UPPER_SNAKE_CASE = "SNAKE_CASE";
    private static final String CONVERSION_PASCAL_CASE = "CamelCase";
    private static final String CONVERSION_CAMEL_CASE = "camelCase";
    private static final String CONVERSION_LOWER_SNAKE_CASE = "snake_case";

    @NotNull
    static String transform(String text,
                            boolean useSpaceCase,
                            boolean useKebabCase,
                            boolean useUpperSnakeCase,
                            boolean usePascalCase,
                            boolean useCamelCase,
                            boolean useLowerSnakeCase,
                            String[] conversionList) {
        String newText;
        boolean repeat;
        int iterations = 0;
        String next = null;

        do {
            newText = text;
            boolean isLowerCase = text.equals(text.toLowerCase());
            boolean isUpperCase = text.equals(text.toUpperCase());

            if (isLowerCase && text.contains("_")) {
                // snake_case to space case
                if (next == null) {
                    next = getNext(CONVERSION_SNAKE_CASE, conversionList);
                    repeat = true;
                } else {
                    if (next.equals(CONVERSION_SPACE_CASE)) {
                        repeat = !useSpaceCase;
                        next = getNext(CONVERSION_SPACE_CASE, conversionList);
                    } else {
                        repeat = true;
                    }
                }
                newText = text.replace('_', ' ');

            } else if (isLowerCase && text.contains(" ")) {
                // space case to kebab-case
                if (next == null) {
                    next = getNext(CONVERSION_SPACE_CASE, conversionList);
                    repeat = true;
                } else {
                    newText = text.replace(' ', '-');
                    if (next.equals(CONVERSION_KEBAB_CASE)) {
                        repeat = !useKebabCase;
                        next = getNext(CONVERSION_KEBAB_CASE, conversionList);
                    } else {
                        repeat = true;
                    }
                }

            } else if (isLowerCase && text.contains("-")) {
                // kebab-case to SNAKE_CASE
                if (next == null) {
                    next = getNext(CONVERSION_KEBAB_CASE, conversionList);
                    repeat = true;
                } else {
                    newText = text.replace('-', '_').toUpperCase();
                    if (next.equals(CONVERSION_UPPER_SNAKE_CASE)) {
                        repeat = !useUpperSnakeCase;
                        next = getNext(CONVERSION_UPPER_SNAKE_CASE, conversionList);
                    } else {
                        repeat = true;
                    }
                }

            } else if (isUpperCase && text.contains("_")) {
                // SNAKE_CASE to PascalCase
                if (next == null) {
                    next = getNext(CONVERSION_UPPER_SNAKE_CASE, conversionList);
                    repeat = true;
                } else {
                    newText = Conversion.toCamelCase(text.toLowerCase());
                    if (next.equals(CONVERSION_PASCAL_CASE)) {
                        repeat = !usePascalCase;
                        next = getNext(CONVERSION_PASCAL_CASE, conversionList);
                    } else {
                        repeat = true;
                    }
                }

            } else if (!isUpperCase && text.substring(0, 1).equals(text.substring(0, 1).toUpperCase()) && !text.contains("_")) {
                // PascalCase to camelCase
                if (next == null) {
                    next = getNext(CONVERSION_PASCAL_CASE, conversionList);
                    repeat = true;
                } else {
                    newText = text.substring(0, 1).toLowerCase() + text.substring(1);
                    if (next.equals(CONVERSION_CAMEL_CASE)) {
                        repeat = !useCamelCase;
                        next = getNext(CONVERSION_CAMEL_CASE, conversionList);
                    } else {
                        repeat = true;
                    }
                }

            } else {
                // camelCase to snake_case
                if (next == null) {
                    next = getNext(CONVERSION_CAMEL_CASE, conversionList);
                    repeat = true;
                } else {
                    newText = Conversion.toSnakeCase(text);
                    if (next.equals(CONVERSION_LOWER_SNAKE_CASE)) {
                        repeat = !useLowerSnakeCase;
                        next = getNext(CONVERSION_LOWER_SNAKE_CASE, conversionList);
                    } else {
                        repeat = true;
                    }
                }

            }
            if (iterations++ > 10) {
                repeat = false;
            }
            text = newText;
        } while (repeat);

        return newText;
    }

    /**
     * Return next conversion (or wrap to first)
     *
     * @param conversion String
     * @param conversions Array of strings
     * @return next conversion
     */
    private static String getNext(String conversion, String[] conversions) {
        int index;
        index = Arrays.asList(conversions).indexOf(conversion) + 1;
        if (index < conversions.length) {
            return conversions[index];
        } else {
            return conversions[0];
        }
    }
    /**
     * Convert a string (CamelCase) to snake_case
     *
     * @param in CamelCase string
     * @return snake_case String
     */
    private static String toSnakeCase(String in) {
        StringBuilder result = new StringBuilder("" + Character.toLowerCase(in.charAt(0)));
        for (int i = 1; i < in.length(); i++) {
            char c = in.charAt(i);
            if (Character.isUpperCase(c)) {
                result.append("_").append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Convert a string (snake_case) to CamelCase
     *
     * @param in snake_case String
     * @return CamelCase string
     */
    private static String toCamelCase(String in) {
        StringBuilder camelCased = new StringBuilder();
        String[] tokens = in.split("_");
        for (String token : tokens) {
            if (token.length() >= 1) {
                camelCased.append(token.substring(0, 1).toUpperCase()).append(token.substring(1));
            } else {
                camelCased.append("_");
            }
        }
        return camelCased.toString();
    }
}
