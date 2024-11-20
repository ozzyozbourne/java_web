package learn;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow;
import java.util.concurrent.ForkJoinPool;

public class _06_Uni_From_Emitter {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("⚡️ Uni from emitter");

       try( ForkJoinPool forkJoinPool = ForkJoinPool.commonPool()){
        CountDownLatch emitterLatch = new CountDownLatch(1);


        // so
        Uni<String> uniFromEmitter = Uni.createFrom()
                .emitter(emitter -> forkJoinPool.submit(() -> {

                emitter.complete("Hello");
                emitterLatch.countDown();
            }));

        uniFromEmitter.subscribe().with(System.out::println);

        emitterLatch.await();
    }

        Multi<Integer> multi = Multi.createFrom().emitter(em -> {
            em.emit(1);
            em.emit(2);
            em.emit(3);

            em.complete();
        });


    }
}
