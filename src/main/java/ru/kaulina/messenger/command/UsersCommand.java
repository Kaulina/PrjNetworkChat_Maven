package ru.kaulina.messenger.command;

import ru.kaulina.messenger.Connection;
import ru.kaulina.messenger.User;
import ru.kaulina.messenger.UserContext;

import java.util.stream.Collectors;

public class UsersCommand extends AbstractCommand {
    public UsersCommand() {
        super("/users");
    }

    @Override
    public void accept(Connection connection, String param) {
        if (connection.getContext().isEmpty()) return;
        UserContext context = connection.getContext().get();

                context.getChatRoom().ifPresent(
                chatRoom -> connection.writeLine(
                        chatRoom.getUsers().stream()
                                .map(UserContext::getUser)
                                .map(User::getName)
                                .collect(Collectors.joining(", "))
                )
        );
    }
}