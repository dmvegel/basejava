package com.javaops.webapp.util;

public class LazySingleton {
//    volatile private static LazySingleton INSTANCE;

    public static LazySingleton getInstance() {
        return LazySingletonHolder.INSTANCE;
//        if (INSTANCE == null) {
//            synchronized (LazySingleton.class) {
//                if (INSTANCE == null) {
//                    INSTANCE = new LazySingleton();
//                }
//            }
//        }
//        return INSTANCE;
    }

    private static class LazySingletonHolder {
        private static final LazySingleton INSTANCE = new LazySingleton();
    }

    private LazySingleton() {
    }
}
