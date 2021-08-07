package comand;

import exception.PathNotFoundException;
import main.ConsoleHelper;
import main.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipAddCommand extends ZipCommand {

    @Override
    public void execute() throws Exception {

        try {
            ConsoleHelper.writeMessage("Adding a new file to an archive.");
            ZipFileManager zipFileManager = getZipFileManager();

            ConsoleHelper.writeMessage("Enter full name of the file to be added.");
            Path path = Paths.get(ConsoleHelper.readString());
            zipFileManager.addFile(path);

            ConsoleHelper.writeMessage("File added to an archive.");

        } catch (PathNotFoundException exception) {
            ConsoleHelper.writeMessage("File not found.");
        }
    }
}
