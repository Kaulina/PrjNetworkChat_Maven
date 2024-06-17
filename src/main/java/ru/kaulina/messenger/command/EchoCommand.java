package ru.kaulina.messenger.command;

import ru.kaulina.messenger.Connection;

public class EchoCommand extends AbstractCommand {

    public EchoCommand() {
        super("/echo");
    }

    @Override
    public void accept(Connection connection, String param) {
        if (param == null || param.isEmpty()) return;
        connection.writeLine(param);
    }
}
