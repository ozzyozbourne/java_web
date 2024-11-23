package BroadCasting;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;


//hot and cold
public final class OneToOne {

    private static final ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "Scheduler-Thread"));

    private static final ExecutorService subscriptionExecutor =
            Executors.newSingleThreadExecutor(r -> new Thread(r, "Subscription-Thread"));

    private static final ExecutorService publisherExecutor =
            Executors.newSingleThreadExecutor(r -> new Thread(r, "Publisher-Thread"));

    private static final CountDownLatch latch = new CountDownLatch(1);

    public static void main(final String... args) throws InterruptedException {
        Infrastructure.setDefaultExecutor(publisherExecutor);

        final Multi<String> publisher = getOneToOneMulti();


        latch.await();
        scheduledExecutorService.close();
        subscriptionExecutor.close();
        publisherExecutor.close();
    }

    static Multi<String> getOneToOneMulti(){
        final AtomicReference<ScheduledFuture<?>> futureAtomicReference = new AtomicReference<>();
        return null;
    }


}


