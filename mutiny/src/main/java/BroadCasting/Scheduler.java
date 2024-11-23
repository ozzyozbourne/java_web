package BroadCasting;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public final class Scheduler {

    private static final AtomicInteger counter1 = new AtomicInteger(1);
    private static final AtomicInteger counter2 = new AtomicInteger(1);
    private static final CountDownLatch latch = new CountDownLatch(2);

    public static void main(final String... args) throws InterruptedException {

        try(final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()){
            scheduledExecutorService.
                    scheduleAtFixedRate(
                            () -> {
                                final int res = counter1.incrementAndGet();
                                System.out.println("In the first one with delay 1 and period 1  -> " + res);
                                if (res == 5) latch.countDown();
                            }, 1, 1, TimeUnit.SECONDS);

            scheduledExecutorService.
                    scheduleAtFixedRate(
                            () -> {
                                final int res = counter2.incrementAndGet();
                                System.out.println("In the Second one with delay 0 and period 1 -> "  + res);
                                if (res == 5) latch.countDown();
                            }, 0, 1, TimeUnit.SECONDS);

            latch.await();
        }

        try(final ExecutorService executorService = Executors.newSingleThreadExecutor()){
            final Future<?> future = executorService .submit(() -> System.out.println());

        }

    }
}
