package com.chrisruffalo.isolation;

import java.io.File;
import java.lang.annotation.Annotation;

public class IsolatedRunner implements Runnable {

    private final Payload payload;
    
    public IsolatedRunner(Payload payload) {
        this.payload = payload;
    }
    
    public void run() {
        // show classloader
        ClassLoader classLoader = IsolatedRunner.class.getClassLoader();
        System.out.printf("Class level loader: %s\n", classLoader);
        
        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        System.out.printf("Context level loader: %s\n", contextLoader.getClass().getName());
        
        new File("file!");
                
        System.out.printf("Built-in loader (1): %s\n", Thread.class.getClassLoader());
        System.out.printf("Built-in loader (2): %s\n", Annotation.class.getClassLoader());
        System.out.printf("Built-in loader (3): %s\n", this.payload.getClass().getClassLoader());
        System.out.printf("Built-in loader (4): %s\n", Payload.class.getClassLoader());
        
        System.out.printf("System loader: %s\n", ClassLoader.getSystemClassLoader());
    }
}
