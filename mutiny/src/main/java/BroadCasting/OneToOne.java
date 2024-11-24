package BroadCasting;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import io.smallrye.mutiny.subscription.BackPressureStrategy;
import io.smallrye.mutiny.subscription.MultiSubscriber;

import java.util.concurrent.*;


//hot and cold
public final class OneToOne {

    private static final ScheduledExecutorService scheduledExecutorService =
            Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "Scheduler-Thread"));

    private static final ExecutorService subscriptionExecutor =
            Executors.newSingleThreadExecutor(r -> new Thread(r, "Subscription-Thread"));

    private static final ExecutorService publisherExecutor =
            Executors.newSingleThreadExecutor(r -> new Thread(r, "Publisher-Thread"));

    private static final CountDownLatch latch = new CountDownLatch(1);

    public static void main(final String... args) throws InterruptedException {
        Infrastructure.setDefaultExecutor(publisherExecutor);

        final Multi<Integer> publisher = getOneToOneMulti();

        publisher.subscribe().withSubscriber(getSubscriber());
        publisher.subscribe().withSubscriber(getSubscriber());
        publisher.subscribe().withSubscriber(getSubscriber());

        latch.await();
        scheduledExecutorService.close();
        subscriptionExecutor.close();
        publisherExecutor.close();
    }

    static Multi<Integer> getOneToOneMulti(){

        final Multi<Integer> stage1 =  Multi.createFrom().emitter(em ->
                scheduledExecutorService.scheduleAtFixedRate(
                        () ->{
                            System.out.println("Emitting on -> " + Thread.currentThread().getName());
                            em.emit(ThreadLocalRandom.current().nextInt());
                            },
                        1,
                        2,
                        TimeUnit.SECONDS), BackPressureStrategy.ERROR);

        return stage1.onOverflow()
                .invoke(() -> System.out.println(Thread.currentThread().getName() + "OVERFLOW"))
                .buffer(10)
                .emitOn(publisherExecutor)
                .runSubscriptionOn(subscriptionExecutor);
    }

    static MultiSubscriber<Integer> getSubscriber(){
        return new MultiSubscriber<>() {

            private Flow.Subscription subscription;
            private int received = 0;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                this.subscription = subscription;
                subscription.request(5);
                // Print thread name to verify different pool
                System.out.println("Subscribing on thread: " +
                        Thread.currentThread().getName());
            }

            @Override
            public void onItem(final Integer item) {
                System.out.println("Processing item: " + item +
                        " on thread: " + Thread.currentThread().getName());
                received += 1;
                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int batchSize = 5;
                if (received % batchSize == 0) {
                    System.out.println("Requesting next batch...");
                    subscription.request(batchSize);
                }
            }


            @Override
            public void onFailure(final Throwable failure) {
                System.out.println("\n✋ " + failure.getMessage());
            }

            @Override
            public void onCompletion() {
                System.out.println("done");
            }

        };
    }


}


