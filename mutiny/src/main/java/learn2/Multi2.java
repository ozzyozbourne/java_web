package learn2;

import io.smallrye.mutiny.Multi;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.TimeUnit;

public class Multi2 {

    public static void main(String[] args) throws InterruptedException {
        test01();
    }

    static void test01() throws InterruptedException {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);

        AtomicReference<ScheduledFuture<?>> ref = new AtomicReference<>();
        AtomicInteger counter = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(1);

        Multi.createFrom().emitter(emitter -> ref.set(service.scheduleAtFixedRate(() -> {
            emitter.emit("tick");
            if (counter.getAndIncrement() == 5) {
                ref.get().cancel(true);
                emitter.complete();
                latch.countDown();
            }
        }, 0, 500, TimeUnit.MILLISECONDS)))
                .subscribe().with(System.out::println, Throwable::printStackTrace, () -> System.out.println("Done!"));

        latch.await();
        service.shutdown();
    }

    static void test02(){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> t  = scheduler.scheduleAtFixedRate(
                () -> System.out.println("asdfa"),
                0,
                30,
                TimeUnit.SECONDS
        );

    }

    static void test03(){

    }
}
