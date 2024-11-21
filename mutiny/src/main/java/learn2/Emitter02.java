package learn2;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Emitter02 {

    public static void main(String[] args) {

        Uni<Integer> uniFromEmitterAndState = Uni.createFrom()
                .emitter(AtomicInteger::new, (i, e) -> e.complete(i.addAndGet(10)));

        for (var i = 0; i < 5; i++) {
            uniFromEmitterAndState.subscribe().with(System.out::println);
        }

        Uni.createFrom().failure(new IOException("Boom")).subscribe().with(System.out::println,
                failure -> System.out.println(failure.getMessage()));

        Multi.createFrom().failure(new IOException("Boom")).subscribe().with(
                System.out::println, // emit channel
                failure -> System.out.println(failure.getMessage()), //failure channel
                 () -> System.out.println("Done")); // done channel
    }
}
