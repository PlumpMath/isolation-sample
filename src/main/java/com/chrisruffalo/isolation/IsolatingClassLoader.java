package com.chrisruffalo.isolation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.weaver.loadtime.ClassLoaderWeavingAdaptor;
import org.aspectj.weaver.loadtime.DefaultWeavingContext;
import org.aspectj.weaver.loadtime.IWeavingContext;

public class IsolatingClassLoader extends ClassLoader {

    private static final String BUILT_IN_JAVA_CLASS_PATTERN = "java\\..*";
    
    private Set<String> whitelistPatterns;
    
    public IsolatingClassLoader() {
        this.whitelistPatterns = new HashSet<String>(0);
    }
    
    public void allow(String... patterns) {
        for(String pattern : patterns) {
            this.whitelistPatterns.add(pattern);
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean arg1) throws ClassNotFoundException {
        System.out.printf("please to load: %s (%s)\n", name, arg1 + "");
        
        if(name == null || name.isEmpty()) {
            return null;
        }
        
        if(name.matches(BUILT_IN_JAVA_CLASS_PATTERN)) {
            return this.loadClassFromExistingClassLoader(name);
        } else {
            // check whitelist and if the class name is 
            // in the whitelist then we can use the class
            // from the existing classloader
            if(this.whitelistPatterns.contains(name)) {
                return this.loadClassFromExistingClassLoader(name);
            } else {
                for(String allow : this.whitelistPatterns) {
                    if(name.matches(allow)) {
                        return this.loadClassFromExistingClassLoader(name);
                    }
                }
            }
            return this.loadClassFromResource(name);
        }
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return this.loadClass(name, false);
    }
     
    private Class<?> loadClassFromExistingClassLoader(String name) throws ClassNotFoundException {
        return super.loadClass(name, true);
    }
    
    private Class<?> loadClassFromResource(String name) throws ClassNotFoundException {        
        // create resource string that corresponds to class
        String classAsPath = name.replace('.', '/') + ".class";
        
        try (
            // get class resource 
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(classAsPath);
                
            // create target byte buffer
            ByteArrayOutputStream output = new ByteArrayOutputStream(stream.available());
        ){
            // create read buffer for input stream
            byte[] readBuffer = new byte[1024];            

            // read stream into read buffer
            int read = stream.read(readBuffer);            
            while(read > 0) {
                // write to output buffer
                output.write(readBuffer, 0, read);
                
                // read next
                read = stream.read(readBuffer);
            }
            
            // get byte array
            byte[] binaryClass = output.toByteArray();
            
            // create weaving adapter
            ClassLoaderWeavingAdaptor adapter = new ClassLoaderWeavingAdaptor();
            
            // create new context
            IWeavingContext context = new DefaultWeavingContext(this);
            
            // init adapter
            adapter.initialize(this, context);
                        
            // weave
            byte[] woven = adapter.weaveClass(name, binaryClass, true);
            
            // define class from woven aspect
            Class<?> loaded = this.defineClass(name, woven, 0, woven.length);
            System.out.println("woven class: " + loaded.getName());
            
            if(loaded != null) {
                return loaded;
            }            
        } catch (IOException e) {
            // ignore (log?)
        }
        
        throw new ClassNotFoundException(name);
    }
}
