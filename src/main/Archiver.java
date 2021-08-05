package main;

import exception.NoSuchZipFileException;

import java.io.IOException;

public class Archiver {

    public static Operation askOperation() throws IOException {
        ConsoleHelper.writeMessage("");
        ConsoleHelper.writeMessage("Select an operation:");
        ConsoleHelper.writeMessage(String.format("\t %d - Zip files to an archive", Operation.CREATE.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - Add a file to an archive", Operation.ADD.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - Remove a file from an archive", Operation.REMOVE.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - Extract an archive", Operation.EXTRACT.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - View the contents of an archive", Operation.CONTENT.ordinal()));
        ConsoleHelper.writeMessage(String.format("\t %d - Exit", Operation.EXIT.ordinal()));

        return Operation.values()[ConsoleHelper.readInt()];
    }

    public static void main(String[] args) throws Exception{
        Operation operation = null;

        do {
            try {
                operation = askOperation();
                CommandExecutor.execute(operation);
            } catch (NoSuchZipFileException e) {
                ConsoleHelper.writeMessage("You didn't select an archive or you selected an invalid file.");
            } catch (Exception e) {
                ConsoleHelper.writeMessage("An error occurred. Please check the entered data.");
            }
        } while(operation != Operation.EXIT);
    }
}
