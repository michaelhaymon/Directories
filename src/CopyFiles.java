import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class CopyFiles extends SimpleFileVisitor<Path> {

    private Path sourceRoot;
    private Path targetRoot;

    public CopyFiles(Path sourceRoot, Path targetRoot) {
        this.sourceRoot = sourceRoot;
        this.targetRoot = targetRoot;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        System.out.println("Error accessing file: " + file.toAbsolutePath() + " " + exc.getMessage());
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path relativizedPath = sourceRoot.relativize(dir);
        System.out.println("relativizedPath = " + relativizedPath);
        Path copyDir = targetRoot.resolve(relativizedPath);
        System.out.println("Resolved Path for copy = " + copyDir);

        try {
            Files.copy(dir, copyDir);
        } catch (IOException e) {
            return FileVisitResult.SKIP_SUBTREE;
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path relativizedPath = sourceRoot.relativize(file);
        System.out.println("relativizedPath = " + relativizedPath);
        Path copyFile = targetRoot.resolve(relativizedPath);
        System.out.println("Resolved Path for copy = " + copyFile);

        try {
            Files.copy(file, copyFile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return FileVisitResult.CONTINUE;
    }
}
