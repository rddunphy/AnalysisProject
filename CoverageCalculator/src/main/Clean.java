package main;

import java.io.File;

public class Clean {

    public static void main(String[] args) {
        deleteDirectory(Main.SOURCE_PROJECT + "/report");
        deleteDirectory(Main.GENERATED_PROJECT + "/ser");
        deleteDirectory(Main.GENERATED_PROJECT + "/src");
        deleteDirectory(Main.GENERATED_PROJECT + "/test");
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
