package com.javaops.webapp;

public class Deadlock {
    static Object lock1 = new Object();
    static Object lock2 = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (lock1) {
                sleep();
                synchronized (lock2) {
                    System.out.println("non reachable");
                }
            }
        }).start();
        new Thread(() -> {
            synchronized (lock2) {
                sleep();
                synchronized (lock1) {
                    System.out.println("non reachable");
                }
            }
        }).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
