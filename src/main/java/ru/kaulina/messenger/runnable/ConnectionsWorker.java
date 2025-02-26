package ru.kaulina.messenger.runnable;

import ru.kaulina.messenger.Connection;
import ru.kaulina.messenger.ConnectionInputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionsWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionsWorker.class);

    protected final Collection<Connection> connectionsStorage;
    protected final Collection<ConnectionInputHandler> handlers;
    protected final AtomicBoolean workPerformed;

    protected long connectionTimeoutMillis = 60_000;

    public ConnectionsWorker(Collection<Connection> connectionsStorage, Collection<ConnectionInputHandler> handlers) {
        this.connectionsStorage = connectionsStorage;
        this.handlers = handlers;
        workPerformed = new AtomicBoolean(false);
    }

    public void setConnectionTimeout(long connectionTimeoutMillis) {
        this.connectionTimeoutMillis = connectionTimeoutMillis;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                workPerformed.set(false);

                Iterator<Connection> iterator = connectionsStorage.iterator();
                while (iterator.hasNext()) {
                    Connection connection = iterator.next();

                    if (connection.isTimeout(connectionTimeoutMillis)) {
                        logger.debug("{} timeout. Closing.", connection);
                        connection.close();
                    }

                    if (connection.isClosed()) {
                        logger.debug("{} is closed. Removing.", connection);
                        iterator.remove();
                        continue;
                    }

                    execute(connection);
                }

                if (!workPerformed.get()) TimeUnit.MILLISECONDS.sleep(1L);
            }
        } catch (InterruptedException ignored) {
        }
    }

    protected void execute(Connection connection) {
        connection.readLine()
                .ifPresent(text -> {
                    workPerformed.set(true);
                    logger.trace("read connection: {}: text='{}'", connection, text);
                    for (ConnectionInputHandler handler : handlers) {
                        if (handler.handle(text, connection)) break;
                    }
                });
    }

}