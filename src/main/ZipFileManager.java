package main;

import exception.NoSuchZipFileException;
import exception.PathNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
}
