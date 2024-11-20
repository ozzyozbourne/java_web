package learn;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.BackPressureStrategy;
import io.smallrye.mutiny.subscription.MultiEmitter;
import io.smallrye.mutiny.subscription.MultiSubscriber;

import java.util.concurrent.Flow;

public class _01_Drop {
    public static void main(String[] args) {
        System.out.println("⚡️ Back-pressure: drop");

        Multi.createFrom().emitter(emitter -> emitTooFast(emitter), BackPressureStrategy.ERROR)
                .onOverflow().buffer(32) // Comment out for some fun
                .subscribe().withSubscriber(new MultiSubscriber<Object>() {
                    @Override
                    public void onSubscribe(Flow.Subscription s) {
                        s.request(5);

                    }

                    @Override
                    public void onItem(Object s) {
                        System.out.print(s + " ");
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println("\n✋ " + throwable.getMessage());
                    }

                    @Override
                    public void onCompletion() {
                        System.out.println("\n✅");
                    }
                });
    }

    private static void emitTooFast(MultiEmitter<? super Object> emitter) {
        new Thread(() -> {
            while (true) {
                emitter.emit("📦");
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
