package comand;

import main.ConsoleHelper;
import main.FileProperties;
import main.ZipFileManager;

import java.util.List;

public class ZipContentCommand extends ZipCommand {

    //Displaying content of archive
    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Viewing contents of the archive.");
        ZipFileManager zipFileManager = getZipFileManager();

        ConsoleHelper.writeMessage("Archive contents:");
        List<FileProperties> files = zipFileManager.getFileList();

        for (FileProperties item : files) {
            ConsoleHelper.writeMessage(item.toString());
        }

        ConsoleHelper.writeMessage("Archive contents viewed.");
    }
}
