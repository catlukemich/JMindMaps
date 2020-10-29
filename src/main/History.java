package main;

import main.commands.Command;

import java.util.ArrayList;
import java.util.List;

public class History {

    public List<Command> commands = new ArrayList<>();
    int command_index = -1;


    public void addCommand(Command command) {
        this.command_index += 1;
        try {
            // Clear the forward history entries:
            this.commands = this.commands.subList(0, this.command_index);

            this.commands.set(this.command_index, command);
        }
        catch (IndexOutOfBoundsException e) {
            this.commands.add(command);
        }
    }

    public void undo(){
        if(this.command_index >= 0) {
            Command last_command = this.commands.get(this.command_index);
            last_command.undo();
            this.command_index -= 1;
        }

    }

    public void redo() {
        if(this.command_index >= -1 && this.command_index < this.commands.size() - 1) {
            this.command_index += 1;
            Command last_command = this.commands.get(this.command_index);
            last_command.redo();
        }
    }

}
