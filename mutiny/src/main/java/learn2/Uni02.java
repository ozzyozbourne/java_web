package learn2;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.smallrye.mutiny.subscription.UniSubscription;

public class Uni02 {

    public static void main(String[] args) {
        var uni = Uni.createFrom().item("Hello, World!");

        uni.subscribe().withSubscriber(new UniSubscriber<String>() {
            @Override
            public void onSubscribe(UniSubscription subscription) {
                System.out.println("onSubscribe");
            }

            @Override
            public void onItem(String item) {
                System.out.println("onItem: " + item);
            }

            @Override
            public void onFailure(Throwable failure) {
                System.out.println("onFailure: " + failure.getMessage());
            }
        });


    }
}
