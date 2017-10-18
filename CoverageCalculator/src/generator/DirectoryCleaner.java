package generator;

import java.io.File;

public class DirectoryCleaner {

    public static void cleanProject(String sourceProject, String generatedProject) {
        deleteDirectory(sourceProject + "/report");
        deleteDirectory(generatedProject + "/ser");
        deleteDirectory(generatedProject + "/src");
        deleteDirectory(generatedProject + "/test");
    }

    public static void deleteDirectory(String path) {
        File file = new File(path);
        deleteDirectory(file);
    }

    private static void deleteDirectory(File file) {
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                deleteDirectory(f);
            }
        }
        if (file.exists() && !file.delete()) {
            System.out.println("Problem deleting file? " + file.getPath());
        }
    }

    public static void main(String[] args) {
        String sourceProject = "ExampleApplication";
        String generatedProject = "Generated";
        cleanProject(sourceProject, generatedProject);
    }

}
