package learn2;

import io.smallrye.mutiny.Uni;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public final class Uni04 {

    public static void main(String[] args) {

        AtomicLong ids = new AtomicLong();
        var uni = Uni.createFrom().item(() -> {
            System.out.println("Creating new AtomicInteger instance");
            return new AtomicInteger();
        }, i -> i.addAndGet(1));
        for(var i = 0; i < 10; i++) uni.subscribe().with(System.out::println);

        var uni_2 = Uni.createFrom().deferred(() -> {
            System.out.println("pae pae pae");
            return Uni.createFrom().item(1+ 1);});

        for (var i = 0; i < 5; i++) {
            uni_2.subscribe().with(System.out::println);
        }
    }
}
