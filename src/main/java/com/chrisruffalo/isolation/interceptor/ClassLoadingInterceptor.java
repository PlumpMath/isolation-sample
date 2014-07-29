package com.chrisruffalo.isolation.interceptor;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ClassLoadingInterceptor {

    @Around(value="call(* Class.getClassLoader())")
    public ClassLoader returnSafeClassLoader() {
        return null;
    }
    
    @Around(value="call(* getSystemClassLoader())")
    public ClassLoader returnSafeSystemClassLoader() {
        return null;
    }
    
    @Around(value="call(* System.exit(..))")
    public void noExit() {
        // no-op
        System.out.println("exit call was intercepted");
    }
    
}
