package learn;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class _CountDownLatch {

    public static void main(String[] args) throws InterruptedException {

        try( final ExecutorService executorService = Executors.newFixedThreadPool(3)){
            final CountDownLatch countDownLatch = new CountDownLatch(100);
            for(int i = 0; i < 100; i++)
                executorService.submit(() -> {
                    try {
                        System.out.println(Thread.currentThread().getName() + " is processing a task...");
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                 } finally {
                        countDownLatch.countDown();
                    }
                });
            countDownLatch.await();
            System.out.println("All tasks have been completed. Proceeding with main thread.");
        }
    }
}
