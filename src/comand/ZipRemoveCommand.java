package comand;

import main.ConsoleHelper;
import main.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipRemoveCommand extends ZipCommand {

    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Removing a file from an archive.");
        ZipFileManager zipFileManager = getZipFileManager();

        ConsoleHelper.writeMessage("Enter full path to the file in the archive.");
        Path path = Paths.get(ConsoleHelper.readString());
        zipFileManager.removeFile(path);

        ConsoleHelper.writeMessage("File removed from the archive.");
    }
}
