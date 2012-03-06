package ru.spbau.korovin.task3;

/**
 *  Exception thrown when expression to evaluate is invalid.
 */
class InvalidExpression extends RuntimeException {
    /**
     * Constructs the exception with message
     * @param s Exception's message
     */
    public InvalidExpression(String s) {
        super(s);
    }
}
