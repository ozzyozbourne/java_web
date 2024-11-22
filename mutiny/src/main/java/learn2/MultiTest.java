package learn2;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.time.Duration;

public class MultiTest {

    public static void main(String[] args) {
        test2();
    }

    static void test3(){
        Multi.createFrom().ticks().every(Duration.ofSeconds(1))
                .onItem().transform(tick -> "Tick: " + tick)
                .select().first(5)
                .subscribe().with(
                        System.out::println,
                        Throwable::printStackTrace,
                        () -> System.out.println("Done!")
                );
    }

    static void test2(){
        Multi.createFrom().items("A", "B", "C", "D")
                .onItem().call(item -> Uni.createFrom().voidItem().onItem().delayIt().by(Duration.ofSeconds(1)))
                .subscribe().with(
                        item -> System.out.println("Item: " + item),
                        Throwable::printStackTrace,
                        () -> System.out.println("Stream finished!")
                );
    }

    static void test1(){
        Multi.createFrom()
                .range(1, 5)
                .onItem()
                .call(item -> Uni.createFrom().voidItem().onItem().delayIt().by(Duration.ofSeconds(1)))
                .subscribe()
                .with(
                        item -> System.out.println("Got: " + item),
                        Throwable::printStackTrace,
                        () -> System.out.println("Done!")
                );
    }
}
