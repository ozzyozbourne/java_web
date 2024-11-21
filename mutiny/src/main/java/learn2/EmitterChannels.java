package learn2;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.BackPressureStrategy;
import io.smallrye.mutiny.subscription.MultiSubscriber;

import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

public final class EmitterChannels {

    public static void main(final String... args) {
        final var emissionPool = Executors.newFixedThreadPool(1);
        final var subscriptionPool = Executors.newFixedThreadPool(1);
        final var emitterPool = Executors.newFixedThreadPool(1);

        Multi.createFrom()
                .emitter(emit -> {
                    emitterPool.execute(() -> {
                        var i = 0;
                        var flag = true;
                        while (flag) {
                            if ( i == 5) { emit.fail(new Exception("you are a failure")); flag = false;}
                            else if (i < 10) { emit.emit("📦");}
                            else { emit.complete(); flag = false; }
                            i += 1;
                            try {
                                Thread.sleep(150);
                            } catch (InterruptedException e) {
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
                        System.out.println("In channel on next");
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
