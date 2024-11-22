package learn2;

import io.smallrye.mutiny.Uni;

import java.util.Optional;

public class Uni07 {

    public static void main(String[] args) {
        System.out.println("⚡️ Misc");

        // this doesnt emit anything
        Uni.createFrom().nothing()
                .subscribe().with(System.out::println, failure -> System.out.println(failure.getMessage()));

        //emits a null
        Uni.createFrom().voidItem()
                .subscribe().with(System.out::println, failure -> System.out.println(failure.getMessage()));

        //this emtis a null
        Uni.createFrom().nullItem()
                .subscribe().with(System.out::println, failure -> System.out.println(failure.getMessage()));

        // Optional style
        Uni.createFrom().optional(Optional.of("Hello"))
                .subscribe().with(System.out::println, failure -> System.out.println(failure.getMessage()));

        Uni.createFrom().optional(Optional.empty())
                .subscribe().with(System.out::println, failure -> System.out.println(failure.getMessage()));

        Uni.createFrom().converter(i -> Uni.createFrom().item("[" + i + "]"), 10)
                .subscribe().with(System.out::println, failure -> System.out.println(failure.getMessage()));

    }
}
