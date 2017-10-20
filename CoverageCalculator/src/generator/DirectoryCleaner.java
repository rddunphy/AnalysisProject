package generator;

import java.io.File;

/**
 * Utility class to remove generated files from output directories.
 */
public class DirectoryCleaner {

    /**
     * Removes files from output directories.
     *
     * @param sourceProject
     * @param generatedProject
     */
    public static void cleanProject(String sourceProject, String generatedProject) {
        System.out.println("  Cleaning output directories...");
        deleteDirectory(sourceProject + "/report");
        deleteDirectory(generatedProject + "/ser");
        deleteDirectory(generatedProject + "/src");
        deleteDirectory(generatedProject + "/test");
    }

    /**
     * Recursively deletes all files in a directory.
     *
     * @param path
     */
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
            System.out.println("  Problem deleting file: " + file.getPath());
        }
    }

    public static void main(String[] args) {
        String sourceProject = "FolioTracker";
        String generatedProject = "Generated";
        cleanProject(sourceProject, generatedProject);
        System.out.println("Output directories cleaned.");
    }

}
