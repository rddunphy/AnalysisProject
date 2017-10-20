package main;

import generator.DirectoryCleaner;
import generator.ProjectGenerator;

import java.io.IOException;

/**
 * @author R. David Dunphy (201424662)
 */
public class Main {

    /**
     * Create an output module which mirrors the source module, but with a test runner and probe
     * calls. Coverage runner can be found at [GeneratedModule]/test/TestCoverageRunner.java. Use
     * the coverage runner to generate a coverage report.
     *
     * @param args
     */
    public static void main(String[] args) {
        String sourceProject = "FolioTracker";
        String generatedProject = "Generated";
        System.out.println("Generating module '" + generatedProject + "' from module '" +
                sourceProject + "'.");
        try {
            DirectoryCleaner.cleanProject(sourceProject, generatedProject);
            ProjectGenerator pg = new ProjectGenerator(sourceProject, generatedProject);
            pg.generate();
            System.out.println("Generating module completed.");
            System.out.println("Coverage runner can be found at '" + generatedProject +
                    "/test/TestCoverageRunner.java'.");
        } catch (IOException e) {
            System.out.println("Generating module aborted due to I/O error.");
        }
    }
}
