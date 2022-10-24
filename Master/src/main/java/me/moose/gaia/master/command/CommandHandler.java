package me.moose.gaia.master.command;

import me.moose.gaia.master.command.impl.TestingCommand;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Moose1301
 * @date 10/23/2022
 */
public class CommandHandler {
    private Map<String, Command> commandMap = new HashMap<>();

    public CommandHandler() {
        commandMap.put("testing", new TestingCommand());
    }

    /**
     * Finds a valid command
     *
     * @param commandLine the command to search for
     * @return the command that was found
     */
    public Command getCommand(String commandLine) {
        Command command = commandMap.get(commandLine);

        if (command == null) {
            return commandMap.values().stream()
                    .filter(filteredCommand -> Arrays.asList(filteredCommand.getAliases())
                            .contains(commandLine))
                    .findFirst()
                    .orElse(null);
        }
        return command;
    }
}
