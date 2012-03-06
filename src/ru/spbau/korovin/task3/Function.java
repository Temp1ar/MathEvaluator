package ru.spbau.korovin.task3;

/**
 * Class for storing information about functions
 */
public class Function {

    private final Character name;
    private final Character variable;
    private final String body;

    /**
     * Creates function with name, local variable and body
     * @param name          Name of the function
     * @param variable      Name of local variable
     * @param body          Body of the function
     */
    public Function(Character name, Character variable, String body) {
        this.name = name;
        this.variable = variable;
        this.body = body;
    }

    /**
     * Returns the body of function
     * @return The body of function
     */
    public String getBody() {
        return body;
    }

    /**
     * Returns the local variable
     * @return The local variable
     */
    public Character getVariable() {
        return variable;
    }

    /**
     * Returns the name of function
     * @return The name of function
     */
    public Character getName() {
        return name;
    }
}
