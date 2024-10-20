package calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
    private static final String DEFAULT_DELIMITERS = ",|:";
    private static final String CUSTOM_DELIMITER_PATTERN = "//(.*?)\\\\n(.*)";
    private String input;
    private List<String> delimiters = new ArrayList<>(Arrays.asList(",", ":"));
    private List<String> parts;

    public Calculator(String input) {
        this.input = input != null ? input : "";
    }

    public String add() {
        double sum = 0;

        if (isEmpty()) {
            return "결과 : 0";
        }

        addCustomDelimiter();
        splitInput();

        for (String num : parts) {
            double number = Double.parseDouble(num);

            if (number < 0) {
                throw new IllegalArgumentException("음수는 허용되지 않습니다: " + number);
            }

            sum += number;
        }

        return formatResult(sum);
    }

    public boolean isEmpty() {
        return input.isEmpty();
    }

    public void addCustomDelimiter() {
        Pattern pattern = Pattern.compile(CUSTOM_DELIMITER_PATTERN);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String customDelimiter = matcher.group(1);
            input = matcher.group(2);
            delimiters.add(Pattern.quote(customDelimiter));
        }
    }

    public void splitInput() {
        String combinedDelimiters = String.join("|", escapeDelimiters(delimiters));
        parts = Arrays.asList(input.split(combinedDelimiters));
        parts = filterEmptyParts(parts);
    }

    private List<String> filterEmptyParts(List<String> parts) {
        List<String> nonEmptyParts = new ArrayList<>();
        for (String part : parts) {
            if (!part.isEmpty()) {
                nonEmptyParts.add(part);
            }
        }
        return nonEmptyParts;
    }

    private static List<String> escapeDelimiters(List<String> delimiter) {
        List<String> escaped = new ArrayList<>();
        for (String deli : delimiter) {
            escaped.add(Pattern.quote(deli));
        }
        return escaped;
    }

    private String formatResult(double sum) {
        return "결과 : " + sum;
    }
}