package test;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.subscription.MultiSubscriber;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

public final class TestPools {

    public static void main(String[] args) {

        // This executor will be used by the publisher to the subscriber the subscription object
        ExecutorService subscriptionPool = Executors.newFixedThreadPool(1, r -> {
            Thread thread = new Thread(r);
            thread.setName("subscription-pool-thread");
            return thread;
        });

        // This executor will be used by the subscription object to send the message into the
        // three channels of the subscriber ie onItem, onComplete and onFailure
        // It gets the values from the emitter then sends to the subscribers
        ExecutorService emitterPool = Executors.newFixedThreadPool(1, r -> {
            Thread thread = new Thread(r);
            thread.setName("emitter-pool-thread");
            return thread;
        });

        // This executor is used by the emitter to generated the value that will be shipped to the
        // subscription obeject so that it can send to the subscribers
        ExecutorService emissionPool = Executors.newFixedThreadPool(1, r -> {
            Thread thread = new Thread(r);
            thread.setName("emission-pool-thread");
            return thread;
        });

        Infrastructure.setDefaultExecutor(emissionPool);

        final Multi<String> stage1 = Multi.createFrom()
                .ticks().every(Duration.ofMillis(1))
                .onItem().transform(tick -> "📦")
                .invoke(tick -> System.out.println("Tick generated on: " +
                        Thread.currentThread().getName()));

        final var stage = stage1
                .onOverflow().buffer(5)
                .invoke(s -> System.out.println("🚨 overflow  " + Thread.currentThread().getName()))
                .emitOn(emitterPool) // Subscription running here
                .runSubscriptionOn(subscriptionPool); //publisher running here

        stage.subscribe()
                .withSubscriber(get());
        stage.subscribe()
                .withSubscriber(get());
        stage.subscribe()
                .withSubscriber(get());
        stage.subscribe()
                .withSubscriber(get());
    }

    // using a batched cold consumer and a hot producer
    static MultiSubscriber<Object> get(){
        return new MultiSubscriber<Object>() {

            private Flow.Subscription subscription;
            private int received = 0;

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
                int batchSize = 5;
                if (received % batchSize == 0) {

                    System.out.println("Requesting next batch...");
                    subscription.request(batchSize);
                }
            }

            @Override
            public void onFailure(Throwable failure) {
                System.out.println("\n✋ buffer full failure -> " + Thread.currentThread().getName());
            }

            @Override
            public void onCompletion() {
                System.out.println("done");
            }
        };
    }
}