package com.gilbut.llmService.Service.Navigation;

import com.gilbut.llmService.DTO.RosMessageDTO.RosMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class NavigationExecutor {
    private final RosService rosService;

    private volatile boolean cancelled = false;
    private volatile CompletableFuture<Void> currentFuture;
    private volatile Runnable currentListener;

    public void cancel() {
        cancelled = true;

        if (currentFuture != null && !currentFuture.isDone()) {
            currentFuture.completeExceptionally(
                    new CancellationException("[NavigationExecutor - cancel] Navigation cancelled")
            );
        }

        if (currentListener != null) {
            rosService.removeArrivedListener(currentListener);
        }
    }

    public CompletableFuture<Void> execute(List<RosMessageDTO> locations) {
        cancelled = false;

        CompletableFuture<Void> future = CompletableFuture.completedFuture(null);

        for (RosMessageDTO loc : locations) {
            future = future.thenCompose(v -> sendAndWait(loc));
        }

        return future;
    }

    private CompletableFuture<Void> sendAndWait(RosMessageDTO location) {
        if (cancelled) {
            return CompletableFuture.failedFuture(
                    new CancellationException("[NavigationExecutor - sendAndWait] Navigation cancelled")
            );
        }

        CompletableFuture<Void> future = new CompletableFuture<>();
        currentFuture = future;

        Runnable listener = new Runnable() {
            @Override
            public void run() {
                if (cancelled) {
                    rosService.removeArrivedListener(this);

                    if (!future.isDone()) {
                        future.completeExceptionally(new CancellationException());
                    }
                    return;
                }

                if (!future.isDone()) {
                    future.complete(null);
                }

                rosService.removeArrivedListener(this);
            }
        };

        currentListener = listener;

        rosService.addArrivedListener(listener);
        rosService.sendMove(location);

        return future.orTimeout(3600, TimeUnit.SECONDS);
    }
}
