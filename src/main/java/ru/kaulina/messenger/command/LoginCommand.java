package ru.kaulina.messenger.command;


import ru.kaulina.messenger.ChatRoom;
import ru.kaulina.messenger.Connection;
import ru.kaulina.messenger.User;
import ru.kaulina.messenger.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoginCommand extends AbstractCommand {
    private static final Logger logger = LoggerFactory.getLogger(LoginCommand.class);
    protected final ChatRoom chatRoom;

    public LoginCommand(ChatRoom chatRoom) {
        super("/login");
        this.chatRoom = chatRoom;
    }

    @Override
    public void accept(Connection connection, String param) {
        if (connection.getContext().isPresent()) return;
        if (param == null || param.isBlank()) return;

        String[] parts = param.split(" ");
        String userName = parts[0];

        User user = new User(userName);

        UserContext context = new UserContext(user, connection);
        connection.setContext(context);

        logger.debug("new {}", context);

        chatRoom.register(context);
        context.setChatRoom(chatRoom);
    }

}