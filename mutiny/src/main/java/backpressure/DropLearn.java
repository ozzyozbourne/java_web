package backpressure;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.BackPressureStrategy;
import io.smallrye.mutiny.subscription.MultiEmitter;
import io.smallrye.mutiny.subscription.MultiSubscriber;

import java.util.concurrent.Flow;

public final class DropLearn {

    public static void main(String[] args) {
        System.out.println("Learning back pressure in mutiny");

        Multi.createFrom().emitter(emmiter -> emmitToFast(emmiter), BackPressureStrategy.DROP   )
                .onOverflow().buffer(5)
                .subscribe().withSubscriber(new MultiSubscriber<Object>() {
                    @Override
                    public void onSubscribe(Flow.Subscription subscription) {
                        subscription.request(5);
                    }

                    @Override
                    public void onItem(Object item) {
                        System.out.print(item + " ");
                    }

                    @Override
                    public void onFailure(Throwable failure) {
                        System.out.println("\n✋ " + failure.getMessage());
                    }

                    @Override
                    public void onCompletion() {
                        System.out.println("\n✅");
                    }
                });
    }

    private static void emmitToFast(final MultiEmitter<? super Object> emitter){
        new Thread(() -> {
           while(true){
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
