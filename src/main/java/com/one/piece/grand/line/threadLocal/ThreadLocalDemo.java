package com.one.piece.grand.line.threadLocal;

import java.util.concurrent.CountDownLatch;

public class ThreadLocalDemo {

    public static void main(String[] args) throws Exception {
        int thread = 3;
        CountDownLatch countDownLatch = new CountDownLatch(thread);
        InnerClass innerClass = new InnerClass();
        for (int i = 0; i < thread; i ++) {
            new Thread(() -> {
                for(int j = 0; j < 4; j++) {
                    innerClass.add(String.valueOf(j));
                    innerClass.print();
                }
                innerClass.set("hello world");
                countDownLatch.countDown();
            }, "thread - " + i).start();
        }
        countDownLatch.await();
    }

    private static class InnerClass{
        public void add(String newStr) {
            StringBuilder stringBuilder = Counter.counter.get();
            Counter.counter.set(stringBuilder.append(newStr));
        }

        public void print() {
            System.out.printf("Thread name: %s, ThreadLocal hashcode:%s, Instance hashcode:%s, Value:%s",
                    Thread.currentThread().getName(),
                    Counter.counter.hashCode(),
                    Counter.counter.get().hashCode(),
                    Counter.counter.get().toString());
        }

        public void set(String words) {
            Counter.counter.set(new StringBuilder(words));
            System.out.printf("Set, Thread name:%s , ThreadLocal hashcode:%s,  Instance hashcode:%s, Value:%s\n",
                    Thread.currentThread().getName(),
                    Counter.counter.hashCode(),
                    Counter.counter.get().hashCode(),
                    Counter.counter.get().toString());
        }
    }


    private static class Counter {
        private static ThreadLocal<StringBuilder> counter = new ThreadLocal<StringBuilder>() {
            @Override
            protected StringBuilder initialValue() {
                return new StringBuilder();
            }
        };
    }
}
