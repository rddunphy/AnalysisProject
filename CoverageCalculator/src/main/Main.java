package main;

import generator.ProjectGenerator;

import java.io.IOException;

public class Main {

    public static final String SOURCE_PROJECT = "ExampleApplication";
    public static final String GENERATED_PROJECT = "Generated";

    public static void main(String[] args) throws IOException {
        ProjectGenerator pg = new ProjectGenerator(SOURCE_PROJECT, GENERATED_PROJECT);
        pg.generate();
    }
}
