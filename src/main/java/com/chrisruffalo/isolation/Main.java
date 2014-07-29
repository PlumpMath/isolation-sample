package com.chrisruffalo.isolation;

public class Main {

    public static final String ISOLATION_GROUP = "isolated";
    
    public void start() {
        // init security manager
        IsolatingSecurityManager.init();
        
        // create isolated group
        IsolationGroup group = IsolationGroup.get();
        
        // create a new root thread in the isolated group
        RootIsolationThread root = new RootIsolationThread(group);

        // start isolation
        root.start();
        try {
            root.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }
    
}
