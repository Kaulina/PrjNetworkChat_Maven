package ru.kaulina.messenger;

public interface ConnectionInputHandler {

    boolean handle(String text, Connection connection);

}