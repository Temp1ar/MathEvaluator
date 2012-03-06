package ru.spbau.korovin.task3;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Evaluator of expressions with functions and variables. Each identifier
 *  should be the latin character. All calculations will be performed in
 *  Integer type. Use case:
 *
 * The evaluation on following input:
 *  <pre><blockquote>
 *  a = -3
 *  b = (3 + a) * l
 *  f(x) = x * x
 *  f(b) / 10 + 7
 *  </blockquote></pre> *
 *  will return 7.
 *  
 */
class Evaluator {

    private class Storage {
        final Map<Character, Integer> variables;
        final Map<Character, Function> functions;

        public Storage() {
            this.variables = new HashMap<>();
            this.functions = new HashMap<>();
        }

        public void addVariable(Character id, Integer value) {
            variables.put(id, value);
        }

        public Integer getVariable(Character id) {
            return variables.get(id);
        }

        public Function getFunction(char name) {
            return functions.get(name);
        }

        public void addFunction(Function f) {
            functions.put(f.getName(), f);
        }
    }

    private final Storage storage = new Storage();
    private String result = "";

    private int calculateSafe(String s) {
        try {
            return calculate(s);
        } catch (StringIndexOutOfBoundsException e) {
            throw new InvalidExpression("Invalid expression: " + s);
        }
    }

    private int calculate(String s) {
        char lastOperator = ' ';
        int rightBracket = 0;
        int lastPos = -1;

        for (int i = s.length() - 1; i >= 0; i--) {
            switch (s.charAt(i)) {
                case ')':
                    rightBracket++;
                    break;
                case '(':
                    rightBracket--;
                    break;
            }

            if (rightBracket > 0)
                continue;

            switch (s.charAt(i)) {
                case '+':
                    return calculate(s.substring(0, i))
                            + calculate(s.substring(i + 1, s.length()));

                case '-':
                    if (i == 0) {
                        return 0
                                - calculate(s.substring(i + 1, s.length()));
                    }
                    return calculate(s.substring(0, i))
                            - calculate(s.substring(i + 1, s.length()));

                case '*':
                    if (lastPos < 0) {
                        lastPos = i;
                        lastOperator = '*';
                    }
                    break;

                case '/':
                    if (lastPos < 0) {
                        lastPos = i;
                        lastOperator = '/';
                    }
                    break;
            }
        }

        if (lastOperator == '*') {
            // Laziness
            int first = calculate(s.substring(0, lastPos));
            if (first == 0) {
                return 0;
            } else {
                return first * calculate(s.substring(lastPos + 1, s.length()));
            }
        }

        if (lastOperator == '/') {
            // Laziness
            int first = calculate(s.substring(0, lastPos));
            if (first == 0) {
                return 0;
            } else {
                return first / calculate(s.substring(lastPos + 1, s.length()));
            }
        }

        if (s.charAt(0) == '(' && s.charAt(s.length() - 1) == ')')
            return calculate(s.substring(1, s.length() - 1));

        return unpackToken(s);
    }

    private int unpackToken(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            Integer var = storage.getVariable(s.charAt(0));
            if (var == null) {
                throw new UndefinedVariable(s.charAt(0));
            }
            return var;
        }
    }

    /**
     * Parses one expression. Evaluates it, if it doesn't contain assignments.
     * @param line Expression to parse.
     */
    public void parse(String line) {
        int equalSignIndex;
        line = StringTools.removeSpaces(line);
        
        if ((equalSignIndex = line.indexOf('=')) == -1) {
            // Final evaluation
            line = replaceFunctionCalls(line);
            result = calculateSafe(line) + "";
        } else {
            // If line contains assignment
            makeAssignment(line, equalSignIndex);
        }
    }

    private void makeAssignment(String line, int equalSignIndex) {
        String id = line.substring(0, equalSignIndex);
        String expr = line.substring(equalSignIndex + 1, line.length());
        if (containsFunction(id)) {
            // Function declaration
            Function f = new Function(id.charAt(0), id.charAt(2), expr);
            storage.addFunction(f);
        } else {
            // Variable declaration
            storage.addVariable(id.charAt(0), calculateSafe(expr));
        }
    }

    private String unpackFunction(char name, String argument) {
        Function f = storage.getFunction(name);
        String body = f.getBody();
        body = body.replace(f.getVariable() + "", argument);
        if(!containsFunction(body)) {
            body = "(" + calculateSafe(body) + ")";
        }
        return body;
    }

    private boolean containsFunction(String body) {
        return body.matches("(.*)[a-z]\\(.*\\)(.*)");
    }

    /**
     * Returns the result of evaluation.
     * @return The result of evaluation.
     */
    public String getResult() {
        return result;
    }

    private String replaceFunctionCalls(String expression) {
        if (!containsFunction(expression)) {
            return expression;
        }

        Pattern fPattern =
                Pattern.compile("[a-z]\\(");
        Matcher matcher = fPattern.matcher(expression);

        int matcherStartingPoint = 0;
        while (matcher.find(matcherStartingPoint)) {
            int startPosition = matcher.start();
            int finishPosition = findClosingBracket(expression,
                    startPosition + 2);

            String token = expression.substring(startPosition,
                finishPosition);

            String evaluated = evaluateFunctionToken(token);

            expression = StringTools.replaceChunkOfString(expression,
                    startPosition, finishPosition, evaluated);

            matcherStartingPoint = startPosition + evaluated.length();
            matcher.reset(expression);
        }

        return expression;
    }



    private String evaluateFunctionToken(String token) {
        Character name = token.charAt(0);
        String argument = token.substring(2, token.length() - 1);
        String unpacked = unpackFunction(name, argument);
        while (containsFunction(unpacked)) {
            unpacked = replaceFunctionCalls(unpacked);
        }
        
        return unpacked;
    }

    private int findClosingBracket(String expression, int openingBracket) {
        int bracketLevel = 0;
        int i = 0;

        try {
            while (bracketLevel >= 0) {
                switch (expression.charAt(openingBracket + i)) {
                    case '(':
                        bracketLevel++;
                        break;
                    case ')':
                        bracketLevel--;
                        break;
                    default:
                        break;
                }
                i++;
            }
        } catch (IndexOutOfBoundsException e) {
            // There's no closing bracket for given opening
            throw new InvalidExpression("Wrong bracket sequence in: "
                    + expression);
        }
        
        return i + openingBracket;
    }
}