package learn;

import io.smallrye.mutiny.Uni;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.CompletableFuture.delayedExecutor;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("⚡️ Uni from CompletionStage");

        var cs = CompletableFuture
                .supplyAsync(() -> "Hello!", delayedExecutor(1, TimeUnit.SECONDS))
                .thenApply(String::toUpperCase);

        Uni.createFrom().completionStage(cs)
                .subscribe().with(System.out::println, failure -> System.out.println(failure.getMessage()));

        Thread.sleep(2000);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    }
}
