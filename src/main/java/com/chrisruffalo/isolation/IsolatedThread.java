package com.chrisruffalo.isolation;

public final class IsolatedThread extends Thread {
   
    public IsolatedThread(ThreadGroup group, Payload payload) {
        super(group, new IsolatedRunner(payload));
    }    
}
