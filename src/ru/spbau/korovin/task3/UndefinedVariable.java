package ru.spbau.korovin.task3;

/**
 * Exception thrown when evaluator met undefined variable in expression.
 */
class UndefinedVariable extends RuntimeException {
    /**
     * Constructs exception for particular variable c.
     * @param c Name of undefined variable.
     */
    public UndefinedVariable(char c) {
        super("Undefined variable: " + c);
    }
}
