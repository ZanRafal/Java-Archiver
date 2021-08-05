package comand;

import main.ConsoleHelper;
import main.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ZipCommand implements Command {

    public ZipFileManager getZipFileManager() throws Exception {
        ConsoleHelper.writeMessage("Please enter the full path to the archive.");
        String text = ConsoleHelper.readString();
        Path filePath = Paths.get(text);
        return new ZipFileManager(filePath);
    }
}
