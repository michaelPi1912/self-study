package com.ai.aiengineer.concurrency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.Test;

/**
 * Bước 1 trong docs/JD/_StudyPlan.md. Điền các TODO, chạy:
 * .\mvnw.cmd test -Dtest=Step1ConcurrencyTest
 * Mỗi test đang fail("TODO") cho tới khi bạn làm xong.
 */
class Step1ConcurrencyTest {

    private static final int ITERATIONS = 100_000;

    /** Chạy cùng một việc trên 2 thread rồi chờ xong. */
    private static void runOnTwoThreads(Runnable task) throws InterruptedException {
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    static class UnsafeCounter {
        int value;

        void increment() {
            value++;
        }
    }

    // Bài 1: tái hiện lost update. Chạy nhiều lần, ghi lại kết quả thực tế vào comment.
    @Test
    void lostUpdate_withPlainInt() throws InterruptedException {
        UnsafeCounter counter = new UnsafeCounter();
        runOnTwoThreads(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                counter.increment();
            }
        });
        // Kỳ vọng 200000 nhưng thường nhỏ hơn. Kết quả các lần chạy của tôi: TODO
        System.out.println("plain int = " + counter.value);
        // Giải thích vì sao value++ không atomic (read - add - write): TODO
    }

    // Bài 2a: synchronized
    @Test
    void fix_withSynchronized() throws InterruptedException {
        // TODO: tạo counter có increment() synchronized, chạy runOnTwoThreads, assertEquals(2 * ITERATIONS, ...)
        SynchronizedCounter counter = new SynchronizedCounter();
        runOnTwoThreads(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                counter.increment();
            }
        });
        
        assertEquals(2 * ITERATIONS, counter.value);
    }
    
    static class SynchronizedCounter {
        int value;

        synchronized void increment() {
            value++;
        }
    }

    // Bài 2b: AtomicInteger
    @Test
    void fix_withAtomicInteger() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger();
        // TODO: chạy runOnTwoThreads với counter.incrementAndGet(), rồi assertEquals
        runOnTwoThreads(() -> {
            for (int i = 0; i < ITERATIONS; i++) {
                counter.getAndIncrement();
            }
        });
        
        assertEquals(2 * ITERATIONS, counter.get());
    }

    // Bài 2c: ReentrantLock (nhớ unlock trong finally)
    @Test
    void fix_withReentrantLock() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        // TODO: lock.lock(); try { ... } finally { lock.unlock(); }
        fail("TODO");
    }

    // Bài 2d: đo thời gian 3 cách bằng System.nanoTime(), in ra rồi so sánh.
    @Test
    void compareTiming() {
        fail("TODO");
    }

    // Bài 3: volatile int vẫn sai. Chứng minh, rồi viết giải thích (visibility vs atomicity) bên dưới.
    @Test
    void volatile_isNotEnough() throws InterruptedException {
        // TODO: dùng `volatile int`, runOnTwoThreads, in kết quả (thường < 200000).
        // Giải thích: TODO
        fail("TODO");
    }

    // Bài 4: ExecutorService + Future
    @Test
    void executorService_collectWithFuture() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            List<Future<Integer>> futures = new ArrayList<>();
            // TODO: submit 10 task, mỗi task trả về i * i
            // TODO: gom bằng future.get(), assert tổng = 285
            fail("TODO");
        } finally {
            pool.shutdown();
        }
    }

    // Bài 4b: CompletableFuture
    @Test
    void completableFuture_combine() {
        // TODO: supplyAsync 10 task, allOf(...).join(), cộng kết quả, assertEquals(285, ...)
        CompletableFuture<Void> placeholder = CompletableFuture.completedFuture(null);
        assertEquals(null, placeholder.join());
        fail("TODO");
    }
}
