package learn;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.UniSubscriber;
import io.smallrye.mutiny.subscription.UniSubscription;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        System.out.println("⚡️ Hello world with UniSubscriber");
        var uni  = Uni.createFrom().item("Hello, World");

        uni.subscribe().withSubscriber(
                new UniSubscriber<String>() {

                    @Override
                    public void onSubscribe(UniSubscription uniSubscription) {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onItem(String s) {
                        System.out.println("onItem: " + s);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        System.out.println("onFailure: " + throwable.getMessage());
                    }
                }
        );

    }
}