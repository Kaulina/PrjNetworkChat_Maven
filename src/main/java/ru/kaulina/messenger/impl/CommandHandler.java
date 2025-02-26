package ru.kaulina.messenger.impl;

import ru.kaulina.messenger.Connection;
import ru.kaulina.messenger.ConnectionInputHandler;
import ru.kaulina.messenger.command.Command;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements ConnectionInputHandler {
    protected final Map<String, Command> commandMap;

    public CommandHandler() {
        commandMap = new HashMap<>();
    }

    public void register(Command command) {
        commandMap.put(command.getName(), command);
    }

    @Override
    public boolean handle(String text, Connection connection) {
        final int maxParts = 2;
        String[] parts = text.split(" ", maxParts);
        String command = parts[0];
        String param = parts.length == maxParts ? parts[1] : "";

        Command cmd = commandMap.get(command);
        if (cmd != null) {
            cmd.accept(connection, param);
            return true;
        }
        return false;
    }

}