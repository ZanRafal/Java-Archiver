package comand;

import exception.PathNotFoundException;
import main.ConsoleHelper;
import main.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipExtractCommand extends ZipCommand {

    @Override
    public void execute() throws Exception {

        try {
            ConsoleHelper.writeMessage("Extracting an archive.");
            ZipFileManager zipFileManager = getZipFileManager();

            ConsoleHelper.writeMessage("Enter full path to the file or directory to be extracted.");
            Path path = Paths.get(ConsoleHelper.readString());
            zipFileManager.extractAll(path);

            ConsoleHelper.writeMessage("Archive extracted.");
        } catch (PathNotFoundException exception) {
            ConsoleHelper.writeMessage("You didn't correctly enter a file name or directory.");
        }
    }
}
