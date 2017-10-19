package main;

import generator.DirectoryCleaner;
import generator.ProjectGenerator;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String sourceProject = "FolioTracker";
        String generatedProject = "Generated";
        DirectoryCleaner.cleanProject(sourceProject, generatedProject);
        ProjectGenerator pg = new ProjectGenerator(sourceProject, generatedProject);
        pg.generate();
    }
}
