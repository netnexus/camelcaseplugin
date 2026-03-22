package de.netnexus.CamelCasePlugin;

import org.apache.commons.lang.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.isUpperCase;

class Conversion {

    static final String CONVERSION_SPACE_CASE = "space case";
    static final String CONVERSION_KEBAB_CASE = "kebab-case";
    static final String CONVERSION_UPPER_SNAKE_CASE = "SNAKE_CASE";
    static final String CONVERSION_PASCAL_CASE = "CamelCase";
    static final String CONVERSION_CAMEL_CASE = "camelCase";
    static final String CONVERSION_PASCAL_CASE_SPACE = "Camel Case";
    static final String CONVERSION_LOWER_SNAKE_CASE = "snake_case";
    static final List<String> ConversionList = List.of(new String[]{"kebab-case", "SNAKE_CASE", "CamelCase", "camelCase", "snake_case", "space case", "Camel Case"});

    @NotNull
    static String transform(String text, String target) {
        String appendText = "";

        Pattern p = Pattern.compile("^\\W+");
        Matcher m = p.matcher(text);
        if (m.find()) {
            appendText = m.group(0);
        }

        int iterations = 0;
        text = text.replaceAll("^\\W+", "");  //remove all special chars
        String current = CaseType(text);

        int idxCurrent = ConversionList.indexOf(current);
        int idxObjectCase = ConversionList.indexOf(target);
        int repeatedTimes = (idxObjectCase + ConversionList.size() - idxCurrent) % ConversionList.size();

        while (iterations++ < repeatedTimes) {
            switch (ConversionList.get(idxCurrent)) {
                // snake_case to space case
                case CONVERSION_LOWER_SNAKE_CASE -> text = text.replace('_', ' ');
                // space case to Camel Case
                case CONVERSION_SPACE_CASE -> text = WordUtils.capitalize(text);
                // Camel Case to kebab-case
                case CONVERSION_PASCAL_CASE_SPACE -> text = text.toLowerCase().replace(' ', '-');
                // kebab-case to SNAKE_CASE
                case CONVERSION_KEBAB_CASE -> text = text.replace('-', '_').toUpperCase();
                // SNAKE_CASE to PascalCase
                case CONVERSION_UPPER_SNAKE_CASE -> text = Conversion.toCamelCase(text.toLowerCase());
                // PascalCase to camelCase
                case CONVERSION_PASCAL_CASE -> text = text.substring(0, 1).toLowerCase() + text.substring(1);
                // camelCase to snake_case
                case CONVERSION_CAMEL_CASE -> text = Conversion.toSnakeCase(text);
                default -> iterations = 8;
            }


            idxCurrent = (idxCurrent+1)%ConversionList.size();
        }

        return appendText + text;
    }

    /**
     * Return next conversion (or wrap to first)
     *
     * @param conversion  String
     * @param conversions Array of strings
     * @return next conversion
     */
    static String getNext(String conversion, String[] conversions) {
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
        in = in.replaceAll(" +", "");
        StringBuilder result = new StringBuilder("" + Character.toLowerCase(in.charAt(0)));
        for (int i = 1; i < in.length(); i++) {
            char c = in.charAt(i);
            if (isUpperCase(c)) {
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
            if (!token.isEmpty()) {
                camelCased.append(token.substring(0, 1).toUpperCase()).append(token.substring(1));
            } else {
                camelCased.append("_");
            }
        }
        return camelCased.toString();
    }

    /**
     * Get a string case type
     *
     * @param text snake_case String
     * @return CaseType string
     */
    static String CaseType(String text) {
        boolean isLowerCase = text.equals(text.toLowerCase());
        boolean isUpperCase = text.equals(text.toUpperCase());

        if (isLowerCase && text.contains("_")) {
            return CONVERSION_LOWER_SNAKE_CASE;
        } else if (isLowerCase && text.contains(" ")) {
            return CONVERSION_SPACE_CASE;
        } else if (isUpperCase(text.charAt(0)) && text.contains(" ")) {
            return CONVERSION_PASCAL_CASE_SPACE;
        } else if (isLowerCase && text.contains("-") || (isLowerCase && !text.contains(" "))) {
            return CONVERSION_KEBAB_CASE;
        } else if ((isUpperCase && text.contains("_")) || (isLowerCase && !text.contains("_") && !text.contains(" ")) || (isUpperCase && !text.contains(" "))) {
            return CONVERSION_UPPER_SNAKE_CASE;
        } else if (!isUpperCase && isUpperCase(text.charAt(0)) && !text.contains("_") && !text.contains(" ")) {
            return CONVERSION_PASCAL_CASE;
        } else {
            return CONVERSION_CAMEL_CASE;
        }
    }
}
