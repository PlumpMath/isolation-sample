package com.chrisruffalo.isolation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public final class RootIsolationRunner implements Runnable {

    public RootIsolationRunner() {

    }    
    
    public void run() {
        // get current thread group
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        
        // debug
        System.out.printf("Current thread group: %s - %s\n", currentGroup.getName(), currentGroup.getClass().getName());
        
        // create isolated child group
        ThreadGroup childIsolation = new ThreadGroup(currentGroup, "isolation-" + UUID.randomUUID());

        // use reflection with isolated classloader
        // to create what should be an isolated thread
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        System.out.printf("root isolated thread class level loader: %s\n", loader.getClass().getName());
        try {
            Class<?> isolatedThreadClass = loader.loadClass(IsolatedThread.class.getName());
            System.out.printf("isolated thread class level loader: %s\n", isolatedThreadClass.getClassLoader().getClass().getName());

            // create payload
            Payload payload = new Payload();

            // construct class
            Constructor<?> constructor = isolatedThreadClass.getConstructor(ThreadGroup.class, Payload.class);
            Object isolated = constructor.newInstance(childIsolation, payload);
            
            // start and join thread
            isolatedThreadClass.getMethod("start", new Class<?>[0]).invoke(isolated, new Object[0]);
            isolatedThreadClass.getMethod("join", new Class<?>[0]).invoke(isolated, new Object[0]);
            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }        
    }
}
