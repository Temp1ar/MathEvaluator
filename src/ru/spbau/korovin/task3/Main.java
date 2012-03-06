package ru.spbau.korovin.task3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Evaluator's work example
 */
public class Main {
    /**
     * First argument is the input file with expressions. Application
     * parses expressions one by one and after evaluating last expression
     * of file prints the result to console.
     * 
     * @param args args[0] should be input file.
     */
    public static void main(String[] args) {
        if(args.length < 1) {
            System.out.println("usage: Evaluator input.txt");
            System.exit(1);
        }

        String file = args[0];

        try
           (BufferedReader reader = new BufferedReader(new FileReader(file)))
        {
            Evaluator evaluator = new Evaluator();
            String line;
            while((line = reader.readLine()) != null) {
                evaluator.parse(line);
            }
            System.out.println(evaluator.getResult());
        } catch(FileNotFoundException e) {
            System.err.println("File not found:" + e.getMessage());
        } catch(IOException e) {
            System.err.println(e);
            e.printStackTrace(System.err);
        }
    }
}
