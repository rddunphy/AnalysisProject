package runtime;

import generator.DirectoryCleaner;
import parser.CODE_UNIT;
import parser.ProjectStructureNode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class ReportGenerator {
    
    private final ReportPageGenerator pageGenerator = new ReportPageGenerator();

    public void generate() {
        try {
            ProjectStructureNode tree = CoverageCalculator.calculate(deserialiseStructure());
            tidyReportDirectory(tree);
            String projectName = tree.getFilePath().substring(0, tree.getFilePath().indexOf("/"));
            generateHtmlFiles(projectName, tree);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void tidyReportDirectory(ProjectStructureNode tree) throws IOException {
        String rootDir = "../" + tree.getFilePath().replace("/src", "/report");
        DirectoryCleaner.deleteDirectory(rootDir);
        File source = new File("../CoverageCalculator/templates/style.css");
        File destination = new File(rootDir + "/style.css");
        if (destination.getParentFile().exists() || destination.getParentFile().mkdirs()) {
            Files.copy(source.toPath(), destination.toPath(), REPLACE_EXISTING);
        }
    }

    private void generateHtmlFiles(String projectName, ProjectStructureNode node) {
        if (node.getType() != CODE_UNIT.METHOD) {
            String reportPath = buildReportPath(node.getFilePath(), node.getType() == CODE_UNIT.CLASS);
            pageGenerator.generatePage(projectName, node, reportPath);
        }
        for (ProjectStructureNode child : node.getChildren()) {
            generateHtmlFiles(projectName, child);
        }
    }

    private static String buildReportPath(String path, boolean isLeaf) {
        path = path.replace("/src", "/report");
        if (isLeaf) {
            path = path.replace(".java", ".html");
            return "../" + path;
        }
        return "../" + path + "/index.html";
    }

    private static ProjectStructureNode deserialiseStructure() throws IOException, ClassNotFoundException {
        try (InputStream streamIn = new FileInputStream("../Generated/ser/structure.ser");
             ObjectInputStream objectinputstream = new ObjectInputStream(streamIn)) {
            return (ProjectStructureNode) objectinputstream.readObject();
        }
    }

}
