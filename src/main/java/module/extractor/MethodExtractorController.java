package module.extractor;

import module.extractor.MethodExtractor;
import module.filefinder.FileFinder;
import java.io.IOException;
import java.util.List;

public class MethodExtractorController {
    private FileFinder fileFinder;
    private MethodExtractor methodExtractor;

    public MethodExtractorController(String project) {
        this.fileFinder = new FileFinder("repos/" + project);
    }

    public void saveMethodsToCSV() throws IOException {
        MethodExtractor.init();

        List<String> javaFiles = fileFinder.findFiles();

        for (String javaFile : javaFiles) {
            MethodExtractor.extract(javaFile);
        }

    }



}
