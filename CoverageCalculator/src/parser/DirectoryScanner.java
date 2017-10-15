package parser;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class DirectoryScanner {

    private Set<String> filePaths;

    public DirectoryScanner() {
        filePaths = new HashSet<>();
    }

    public Set<String> scan(String root) {
        return scan(new File(root));
    }

    public Set<String> scan(File root) {
        return scan("", root);
    }

    private Set<String> scan(String path, File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                scan(path + "/" + child.getName(), child);
            }
        } else {
            if (path.endsWith(".java")) {
                filePaths.add(path);
            }
        }
        return filePaths;
    }
}
