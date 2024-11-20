package learn2;

import io.smallrye.mutiny.Uni;

public class Uni01 {

    public static void main(String[] args) {
        var uni = Uni.createFrom().item("Hello World");
        uni.subscribe().with(System.out::println);
    }
}
