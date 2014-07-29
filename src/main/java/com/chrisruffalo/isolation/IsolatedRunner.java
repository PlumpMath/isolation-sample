package com.chrisruffalo.isolation;

import java.io.File;
import java.lang.annotation.Annotation;

public class IsolatedRunner implements Runnable {

    private final Payload payload;
    
    public IsolatedRunner(Payload payload) {
        this.payload = payload;
    }
    
    public void run() {
        // get local loader
        ClassLoader localLoader = this.getClass().getClassLoader();
        System.out.printf("Local loader: %s\n", localLoader);
        
        // show classloader
        ClassLoader classLoader = IsolatedRunner.class.getClassLoader();
        System.out.printf("Class level loader: %s\n", classLoader);
        
        ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
        System.out.printf("Context level loader: %s\n", contextLoader.getClass().getName());
        
        // create file
        new File("file!");
                
        // check plausible sources of external/basic classloader
        System.out.printf("Built-in loader (1): %s\n", Thread.class.getClassLoader());
        System.out.printf("Built-in loader (2): %s\n", Annotation.class.getClassLoader());
        System.out.printf("Built-in loader (3): %s\n", this.payload.getClass().getClassLoader());
        System.out.printf("Built-in loader (4): %s\n", Payload.class.getClassLoader());
        
        System.out.printf("System loader: %s\n", ClassLoader.getSystemClassLoader());
        
        // can't exit either
        System.exit(0);
    }
}
