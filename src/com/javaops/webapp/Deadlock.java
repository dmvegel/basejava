package com.javaops.webapp;

public class Deadlock {
    static Object lock1 = new Object();
    static Object lock2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> run(lock1, lock2)).start();
        new Thread(() -> run(lock2, lock1)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void run(Object lock1, Object lock2) {
        synchronized (lock1) {
            sleep();
            synchronized (lock2) {
                System.out.println("non reachable");
            }
        }
    }
}
