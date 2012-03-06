package ru.spbau.korovin.task3;

/**
 * Some useful string tools
 */
class StringTools {
    /**
     * Replaces fragment of string, from the starting position startPosition,
     * to finish position finishPosition with string replacement.
     * @param string            Target string
     * @param startPosition     Starting position
     * @param finishPosition    Finish position
     * @param replacement       Replacement string
     * @return                  String with replaced fragment.
     */
    public static String replaceChunkOfString(String string, int startPosition,
                                        int finishPosition, String replacement){
        string =
                string.substring(0, startPosition) +
                        replacement + string.substring(finishPosition,
                        string.length());
        return string;
    }

    /**
     * Removes all spaces from string.
     * @param s Target string.
     * @return  String with removed spaces.
     */
    public static String removeSpaces(String s) {
        return s.replaceAll(" ", "");
    }
}
