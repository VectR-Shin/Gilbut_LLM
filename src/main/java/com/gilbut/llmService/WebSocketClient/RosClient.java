package com.gilbut.llmService.WebSocketClient;

import java.util.function.Consumer;

public interface RosClient {
    void connect() throws Exception;
    boolean isOpen();
    void send(String message);
    void reconnect() throws Exception;

    void setOnOpen(Runnable callback);
    void setOnClose(Consumer<Integer> closeCodeConsumer);
    void setOnError(Consumer<Exception> callback);
}
