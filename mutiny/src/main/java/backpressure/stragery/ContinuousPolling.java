package backpressure.stragery;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.BackPressureStrategy;
import io.smallrye.mutiny.subscription.MultiSubscriber;

import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

public final class ContinuousPolling {

    public static void main(final String... args) {

        final var emissionPool = Executors.newFixedThreadPool(1);
        final var subscriptionPool = Executors.newFixedThreadPool(1);
        final var emitterPool = Executors.newFixedThreadPool(1);

        Multi.createFrom()
                .emitter(emit -> {
                    emitterPool.execute(() -> {
                        while (true) {
                            emit.emit("📦");
                            try {
                                Thread.sleep(150);
                            } catch (InterruptedException e) {
                                emit.fail(e);
                                e.printStackTrace();
                            }
                        }
                    });
                }, BackPressureStrategy.ERROR)
                .emitOn(emissionPool)
                .runSubscriptionOn(subscriptionPool)
                .subscribe()
                .withSubscriber(new MultiSubscriber<Object>() {

                    @Override
                    public void onSubscribe(Flow.Subscription subscription) {
                        subscription.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onItem(Object item) {
                        System.out.println("In channel on next -> " + item);
                    }

                    @Override
                    public void onFailure(Throwable failure) {
                        System.out.println("In channel failure -> "  + failure.getMessage());
                    }

                    @Override
                    public void onCompletion() {
                        System.out.println("In channel completed");
                    }
                });
    }
}
