package main;

import generator.ProjectGenerator;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String sourceProject = "ExampleApplication";
        String generatedProject = "Generated";
        ProjectGenerator pg = new ProjectGenerator(sourceProject, generatedProject);
        pg.generate();
    }
}
