package learn;

import io.smallrye.mutiny.Uni;

import java.util.Random;

public class _03_Uni_From_Supplier {

    public static void main(String[] args) {
        System.out.println("️⚡️ Uni from supplier");

        Random random = new Random();
        Uni<Integer> uniFromSupplier = Uni.createFrom().item(random::nextInt);
        for (int i = 0; i < 10; i++) {
            uniFromSupplier.subscribe().with(System.out::println);
        }
    }
}
