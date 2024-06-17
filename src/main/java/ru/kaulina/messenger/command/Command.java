package ru.kaulina.messenger.command;

import ru.kaulina.messenger.Connection;

import java.util.function.BiConsumer;

public interface Command extends BiConsumer<Connection, String> {
    String getName();

    @Override
    void accept(Connection connection, String param);
}