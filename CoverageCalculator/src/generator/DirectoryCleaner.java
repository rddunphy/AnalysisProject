package generator;

import main.Main;

import java.io.File;

public class DirectoryCleaner {

    public static void cleanProject(String sourceProject, String generatedProject) {
        deleteDirectory(sourceProject + "/report");
        deleteDirectory(generatedProject + "/ser");
        deleteDirectory(generatedProject + "/src");
        deleteDirectory(generatedProject + "/test");
    }

    private static void deleteDirectory(String path) {
        File file = new File(path);
        deleteDirectory(file);
    }

    private static void deleteDirectory(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteDirectory(f);
            }
        }
        file.delete();
    }
}
