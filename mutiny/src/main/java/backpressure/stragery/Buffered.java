package backpressure.stragery;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.BackPressureStrategy;
import io.smallrye.mutiny.subscription.MultiEmitter;
import io.smallrye.mutiny.subscription.MultiSubscriber;

import java.util.concurrent.Flow;

//this simulates golang buffered channel like behavior
public final class Buffered {

    public static void main(String[] args) {
        Multi.createFrom().emitter(emit -> emitTooFast(emit), BackPressureStrategy.ERROR)
                .onOverflow().buffer(10)
                .subscribe().withSubscriber(new MultiSubscriber<Object>() {
                    @Override
                    public void onSubscribe(Flow.Subscription subscription) {
                        subscription.request(5);
                    }

                    @Override
                    public void onItem(Object item) {
                        System.out.printf("%s ", item);
                    }

                    @Override
                    public void onFailure(Throwable failure) {
                        throw new RuntimeException(failure);
                    }

                    @Override
                    public void onCompletion() {
                        System.out.println("done");
                    }
                });
    }

    private static void emitTooFast(final MultiEmitter<? super Object> emitter) {
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
