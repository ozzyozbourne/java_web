package backpressure.stragery;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.BackPressureStrategy;

public final class MultiSubscriberTest {

    public static void main(String[] args) {
        // Create Multi with Thread-based emitter
        Multi<String> producer = Multi.createFrom()
                .emitter(emit -> new Thread(() -> {
                    System.out.println("Emitter starting on thread: " +
                            Thread.currentThread().getName());
                    while (true) {
                        emit.emit("📦");
                        try {
                            Thread.sleep(150);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start(), BackPressureStrategy.ERROR);

        // First subscriber
        producer.subscribe().with(
                item -> System.out.println("Subscriber 1 got: " + item +
                        " on thread: " + Thread.currentThread().getName())
        );

        // Second subscriber
        producer.subscribe().with(
                item -> System.out.println("Subscriber 2 got: " + item +
                        " on thread: " + Thread.currentThread().getName())
        );
    }
}