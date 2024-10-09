package module.filefinder;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FileFinder {

    private Path rootDir;
    public FileFinder(String rootDir) {
        this.rootDir = Paths.get(rootDir);
    }
    public List<String> findFiles() throws IOException {
        // ファイルを検索する処理
        List<Path> javaFiles = new ArrayList<>();

        Files.walkFileTree(rootDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                // ".java" 拡張子を持ち、パスに "test" を含まないファイルを取得
                if (file.toString().endsWith(".java") && !file.toString().toLowerCase().contains("test")) {
                    javaFiles.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return javaFiles.stream().map(Path::toString).toList();
    }
}
