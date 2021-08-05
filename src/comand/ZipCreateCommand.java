package comand;

import main.ConsoleHelper;
import main.ZipFileManager;

import java.nio.file.Path;

public class ZipCreateCommand extends ZipCommand {

    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Creating an archive.");
    }
}
