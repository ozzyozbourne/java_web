package backpressure.stragery;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.BackPressureStrategy;
import io.smallrye.mutiny.subscription.MultiSubscriber;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

public final class SlowBufferedConsumer {

    public static void main(String[] args) {

        System.out.println("Main thread -> " + Thread.currentThread().getName());

        ExecutorService emissionPool = Executors.newFixedThreadPool(1);
        ExecutorService subscriptionPool = Executors.newFixedThreadPool(1);
        ExecutorService emitterPool = Executors.newFixedThreadPool(1);

        Multi.createFrom()
                .emitter(emit -> {
                    // Use separate pool for emitter
                    emitterPool.execute(() -> {
                        System.out.println("Emitting items on thread: " +
                                Thread.currentThread().getName());
                        while (true) {
                            emit.emit("📦");
                            try {
                                Thread.sleep(150);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }, BackPressureStrategy.ERROR)
                .onOverflow()
                .invoke(s -> System.out.print("🚨 "))
                .buffer(100)
                // Run emission on one pool
                .emitOn(emissionPool)
                // Run subscription (consumer) on different pool
                .runSubscriptionOn(subscriptionPool)
                .subscribe()
                .withSubscriber(new MultiSubscriber<Object>() {

                    private Flow.Subscription subscription;
                    private int received = 0;
                    private final int batchSize = 5;

                    @Override
                    public void onSubscribe(Flow.Subscription subscription) {
                        this.subscription = subscription;
                        subscription.request(5);
                        // Print thread name to verify different pool
                        System.out.println("Subscribing on thread: " +
                                Thread.currentThread().getName());
                    }

                    @Override
                    public void onItem(Object item) {
                        // Print thread name to verify different pool
                        System.out.println("Processing item: " + item +
                                " on thread: " + Thread.currentThread().getName());
                        received++;
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (received % batchSize == 0) {

                            System.out.println("Requesting next batch...");
                            subscription.request(batchSize);
                        }
                    }

                    @Override
                    public void onFailure(Throwable failure) {
                        System.out.println("\n✋ " + failure.getMessage());
                    }

                    @Override
                    public void onCompletion() {
                        System.out.println("done");
                    }
                });

    }


}