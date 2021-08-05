package comand;

import exception.PathNotFoundException;
import main.ConsoleHelper;
import main.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipCreateCommand extends ZipCommand {

    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Creating an archive.");
            ZipFileManager zipFileManager = getZipFileManager();
            ConsoleHelper.writeMessage("Enter full path to the file or directory to be zipped.");
            Path path = Paths.get(ConsoleHelper.readString());
            zipFileManager.createZip(path);
            ConsoleHelper.writeMessage("Archive created.");
        } catch (PathNotFoundException exception) {
            ConsoleHelper.writeMessage("You didn't correctly enter a file name or directory.");
        }
    }
}
