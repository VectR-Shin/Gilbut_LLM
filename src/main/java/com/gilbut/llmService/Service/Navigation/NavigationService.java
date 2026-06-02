package com.gilbut.llmService.Service.Navigation;

import com.gilbut.llmService.DTO.NavigationCommand.NavigationCommand;
import com.gilbut.llmService.DTO.RosMessageDTO.RosMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class NavigationService {
    private final NavigationExecutor executor;
    private final RosService rosService;
    private final TaskScheduler taskScheduler;

    private final NavigationCommand command = new NavigationCommand();

    private volatile CompletableFuture<Void> currentTask;

    // NAVIGATION
    public synchronized void navigate(List<RosMessageDTO> route) {
        cancelInternalAsync()
                .thenRun(() -> command.setNewRoute(route))
                .thenCompose(v -> startExecutionAsync())
                .thenRun(() -> log.info("[NavigationService - navigate()] navigate() 완료"));
    }

    // ADD_WAYPOINT
    public synchronized void addWaypoint(List<RosMessageDTO> waypoints) {
        cancelInternalAsync()
                .thenRun(() -> command.addWaypoint(waypoints))
                .thenCompose(v -> startExecutionAsync())
                .thenRun(() -> log.info("[NavigationService - addWaypoint()] addWaypoint() 완료"));

    }

    // CANCEL
    public synchronized void cancel() {
        cancelInternalAsync()
                .thenRun(() -> {
                    command.clear();
                    log.info("[NavigationService - cancel()] cancel() 완료");
                });
    }

    private CompletableFuture<Void> startExecutionAsync() {
        if (command.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        currentTask = executor.execute(command.getRoute());

        return currentTask
                .thenRun(() -> {log.info("[NavigationService - startExecutionAsync()] Navigation 전송 완료");})
                .exceptionally(e -> {
                    Throwable cause = (e instanceof CompletionException) ? e.getCause() : e;

                    if (cause instanceof CancellationException) {
                        log.info("[NavigationService - startExecutionAsync()] Navigation 취소");
                    } else {
                        log.warn("[NavigationService - startExecutionAsync()] Navigation 요청 중 예외 발생: {}", e.getMessage());
                    }
                    return null;
                });
    }

    private CompletableFuture<Void> cancelInternalAsync() {
        executor.cancel();

        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(true);
        }

        rosService.sendCancel();

        CompletableFuture<Void> future = new CompletableFuture<>();

        taskScheduler.schedule(() -> {

            for (int i = 0; i < 3; i++) {

                int delay = i * 50;

                if (i == 2) {
                    taskScheduler.schedule(() -> {
                        rosService.sendStop();

                        log.info("[NavigationService - cancelInternalAsync()] cancelInternalAsync 완료");

                        future.complete(null);
                    }, java.time.Instant.now().plusMillis(delay));

                } else {
                    taskScheduler.schedule(
                            rosService::sendStop,
                            java.time.Instant.now().plusMillis(delay)
                    );
                }
            }

        }, java.time.Instant.now().plusMillis(100));

        return future;
    }
}
