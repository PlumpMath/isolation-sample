package com.chrisruffalo.isolation;

public final class RootIsolationThread extends Thread {

    public RootIsolationThread(IsolationGroup parent) {
        super(parent, new RootIsolationRunner());
        
        System.out.printf("proposed thread group: %s\n", parent);
        
        // create isolated class loader
        IsolatingClassLoader loader = new IsolatingClassLoader();
        
        // add to whitelist
        loader.allow(Payload.class.getName());
        
        // set isolated class loader as the main loader for this thread
        this.setContextClassLoader(loader);
    }
    
}
