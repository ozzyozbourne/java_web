package learn;

import io.smallrye.mutiny.Uni;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class _04_Uni_From_Supplier_And_State {

    public static void main(String[] args) {
        System.out.println("️⚡️ Uni from supplier with state");

        Uni<Integer> uniFromSupplierAndState = Uni.createFrom().item(AtomicInteger::new, i -> i.addAndGet(10));

        for (var i = 0; i < 5; i++) {
            uniFromSupplierAndState.subscribe().with(System.out::println);
        }

        Uni<Integer> uniFromDeferred = Uni.createFrom()
                .deferred(() -> Uni.createFrom().item(AtomicInteger::new, i -> i.addAndGet(10)));

        for (var i = 0; i < 5; i++) {
            uniFromDeferred.subscribe().with(System.out::println);
        }

        Supplier<AtomicInteger> test = AtomicInteger::new;

        var one = test.get();
        var two = test.get();

        System.out.println(one + "  " + two + " " );

        one.addAndGet(10);

        System.out.println(one + "  " + two + " " );
    }
}
