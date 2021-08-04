package comand;

import main.ConsoleHelper;

public class ExitCommand implements Command {

    @Override
    public void execute() throws Exception {
        ConsoleHelper.writeMessage("Bye!");
    }
}
