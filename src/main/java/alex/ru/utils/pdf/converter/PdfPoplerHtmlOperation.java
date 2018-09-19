package alex.ru.utils.pdf.converter;

import org.springframework.lang.NonNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PdfPoplerHtmlOperation implements PdfOperation {

    @Override
    public String exec(@NonNull final String pathToTmpDir,@NonNull final String fileName) {

        try {
            final String zipDirectory = pathToTmpDir + "/" + UUID.randomUUID().toString();
            final Path path = Paths.get(zipDirectory);
            final Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwx------");
            final FileAttribute<Set<PosixFilePermission>> attributes = PosixFilePermissions.asFileAttribute(perms);

            Files.createDirectory(path, attributes);
            Files.move(Paths.get(pathToTmpDir + "/"+ fileName),
                    Paths.get(zipDirectory + "/" + fileName), StandardCopyOption.ATOMIC_MOVE);

            final ProcessExecutor processExecutor = new ProcessExecutor();

            boolean isExecuted = processExecutor.exec(String.format("/usr/bin/pdftohtml -i %s/%s", zipDirectory, fileName));
            final String zipFile = zipDirectory + "/" + fileName + ".zip";
            zipArchive(Paths.get(zipFile));

            if(isExecuted)
                return zipFile;
            else
                return null;

        } catch (NullPointerException |IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void zipArchive(final Path path) throws IOException {

        final ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(path.toFile().getAbsolutePath()));
        final File parent = path.getParent().toFile();
        final File[] files = parent.listFiles();
        try {
            for(final File file : files) {
                if(file.getName().contains(".html")) {
                    final FileInputStream inputStream = new FileInputStream(file);
                    final ZipEntry zipEntry = new ZipEntry(file.getName());
                    outputStream.putNextEntry(zipEntry);
                    byte[] buffer = new byte[1024];
                    int offSet;
                    while((offSet = inputStream.read(buffer)) >= 0) {
                        outputStream.write(buffer, 0 , offSet);
                    }
                    outputStream.closeEntry();
                    inputStream.close();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }
}
