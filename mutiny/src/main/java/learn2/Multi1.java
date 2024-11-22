package learn2;

import io.smallrye.mutiny.Multi;

import java.util.concurrent.Flow;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class Multi1 {

    public static void main(String[] args) {
            test01();
    }

    // Implement all four behaviours of the subscriber class ie onSubscription, onItem, onFailure, onComplete
    static void test01(){
        Multi.createFrom().items(1, 2, 3)
                .subscribe().with(
                        subscription -> {
                            System.out.println("Subscription: " + subscription);
                            subscription.request(1);
                        },
                        item -> System.out.println("Item: " + item),
                        failure -> System.out.println("Failure: " + failure.getMessage()),
                        () -> System.out.println("Completed"));
    }

    // This one as only one behaviour defined for a subscriber ie on item
    static void test02(){
        Multi.createFrom().range(10, 15)
                .subscribe().with(System.out::println);
    }


    static void test03(){
       final var randomNumbers = Stream
                .generate(ThreadLocalRandom.current()::nextInt)
                .limit(5)
                .toList();

        Multi.createFrom().iterable(randomNumbers)
                .subscribe().with(System.out::println);
    }

    // Using anonymous class instead of a functions for class behavior
    static void test04(){
        Multi.createFrom().items(1, 2, 3).subscribe().withSubscriber(new Flow.Subscriber<Integer>() {
            private Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                System.out.println("onSubscribe()");
                this.subscription = subscription;
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext: " + integer);
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("onError: " + t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete()");
            }
        });
    }


}
