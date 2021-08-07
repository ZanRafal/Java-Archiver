package main;

import exception.NoSuchZipFileException;
import exception.PathNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    public void createZip(Path source) throws Exception {
        Path zipDirectory = zipFile.getParent();

        //creating new directory if zip directory does not exist
        if(Files.notExists(zipDirectory)) {
            Files.createDirectories(zipDirectory);
        }

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile))){

            if(Files.isDirectory(source)) {
                FileManager fileManager = new FileManager(source);
                List<Path> fileNames = fileManager.getFileList();

                for (Path fileName : fileNames) {
                    addNewZipEntry(zipOutputStream, source, fileName);
                }
            } else if (Files.isRegularFile(source)) {
                addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());
            } else {
                throw new PathNotFoundException();
            }
        }
    }

    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception {
        Path fullPath = filePath.resolve(fileName);

        try (InputStream inputStream = Files.newInputStream(fullPath)) {
            ZipEntry zipEntry = new ZipEntry(fileName.toString());

            zipOutputStream.putNextEntry(zipEntry);
            copyData(inputStream, zipOutputStream);

            zipOutputStream.closeEntry();
        }
    }

    //method used to copy the data from input stream to output stream
    private void copyData(InputStream in, OutputStream out) throws Exception {
        byte[] buffer = new byte[8 * 1024];
        int length;

        while ((length = in.read(buffer)) > 0 ){
            out.write(buffer, 0, length);
        }
    }

    //Method that returns list of the files in the archive
    public List<FileProperties> getFileList() throws Exception {

        //checking if the zip file exists
        if(!Files.isRegularFile(zipFile)) {
            throw new NoSuchZipFileException();
        }

        List<FileProperties> filePropertiesList = new ArrayList<>();

        try(ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
          ZipEntry zipEntry = zipInputStream.getNextEntry();

          while (zipEntry != null) {
              ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
              copyData(zipInputStream, byteArrayOutputStream);

              FileProperties file = new FileProperties(zipEntry.getName(), zipEntry.getSize(), zipEntry.getCompressedSize(), zipEntry.getMethod());
              filePropertiesList.add(file);

              zipEntry = zipInputStream.getNextEntry();
          }
        }
        return filePropertiesList;
    }

    public void extractAll(Path outputFolder) throws Exception {

        //checking if the zip file exists
        if(!Files.isRegularFile(zipFile)) {
            throw new NoSuchZipFileException();
        }

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {

            //if output folder does not exist create new one
            if(Files.notExists(outputFolder)) {
                Files.createDirectories(outputFolder);
            }

            //Going through the contents of the zip stream
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                //Relative path inside of the archive. Name of the file
                String fileName = zipEntry.getName();
                //Some files may be inside folders so we need to resolve the correct path
                Path fileFullName = outputFolder.resolve(fileName);

                //Creating necessary directories
                Path parentPath = fileFullName.getParent();

                //checking whether parent file exists, if not than create new dir.
                if(Files.notExists(parentPath)) {
                    Files.createDirectories(parentPath);
                }

                try (OutputStream outputStream = Files.newOutputStream(fileFullName)) {
                    copyData(zipInputStream, outputStream);
                }

                zipEntry = zipInputStream.getNextEntry();
            }
        }
    }

    public void removeFiles(List<Path> pathList) throws Exception {

        //checking if the zip file exists
        if(!Files.isRegularFile(zipFile)) {
            throw new NoSuchZipFileException();
        }

        //Creating temporary file
        Path tempFile = Files.createTempFile(null, null);

        try(ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempFile))) {
            try(ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {

                ZipEntry zipEntry = zipInputStream.getNextEntry();

                while (zipEntry != null) {

                    Path archivedFile = Paths.get(zipEntry.getName());

                    if(!pathList.contains(archivedFile)) {
                        String fileName = zipEntry.getName();
                        zipOutputStream.putNextEntry(new ZipEntry(fileName));

                        copyData(zipInputStream, zipOutputStream);

                        zipOutputStream.closeEntry();
                        zipInputStream.closeEntry();
                    } else {
                        ConsoleHelper.writeMessage(String.format("File '%s' was removed from the archive.", archivedFile.toString()));
                    }
                    zipEntry = zipInputStream.getNextEntry();
                }
            }
        }

        //Move temporary archive to the location of the original
        Files.move(tempFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }

    public void removeFile(Path path) throws Exception {
        removeFiles(Collections.singletonList(path));
    }

    public void addFiles(List<Path> absolutePathList) throws Exception {

        //checking if the zip file exists
        if(!Files.isRegularFile(zipFile)) {
            throw new NoSuchZipFileException();
        }

        Path tempZipFile = Files.createTempFile(null, null);
        List<Path> archiveFiles = new ArrayList<>();

        try(ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempZipFile))) {
            try(ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {

                ZipEntry zipEntry = zipInputStream.getNextEntry();

                while (zipEntry != null) {
                    String fileName = zipEntry.getName();
                    archiveFiles.add(Paths.get(fileName));

                    zipOutputStream.putNextEntry(new ZipEntry(fileName));
                    copyData(zipInputStream, zipOutputStream);

                    zipOutputStream.closeEntry();
                    zipInputStream.closeEntry();

                    zipEntry = zipInputStream.getNextEntry();
                }

                //Archive new files
                for (Path file : absolutePathList) {
                    if (Files.isRegularFile(file)) {
                        if (archiveFiles.contains(file.getFileName()))
                            ConsoleHelper.writeMessage(String.format("File '%s' already exists in archive.", file.toString()));
                        else {
                            addNewZipEntry(zipOutputStream, file.getParent(), file.getFileName());
                            ConsoleHelper.writeMessage(String.format("File '%s' was added to the archive.", file.toString()));
                        }
                    } else
                        throw new PathNotFoundException();
                }
            }
            Files.move(tempZipFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    
}