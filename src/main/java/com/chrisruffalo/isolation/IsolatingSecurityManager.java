package com.chrisruffalo.isolation;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

public class IsolatingSecurityManager extends SecurityManager {

    public static void init() {
        SecurityManager manager = System.getSecurityManager();
        final boolean defaultPermit = manager == null;
        System.out.printf("creating new security manager with defaultPermit:%s\n", defaultPermit + "");
        manager = new IsolatingSecurityManager(defaultPermit);
        System.setSecurityManager(manager);
    }
    
    private final boolean defaultPermit;
    
    private IsolatingSecurityManager(final boolean defaultPermit) {
        this.defaultPermit = defaultPermit;
    }

    @Override
    public void checkAccept(String host, int port) {
     
        if(!this.defaultPermit) {
            super.checkAccept(host, port);
        }
    }

    @Override
    public void checkAccess(Thread t) {

        if(!this.defaultPermit) {
            super.checkAccess(t);
        }
    }

    @Override
    public void checkAccess(ThreadGroup g) {

        if(!this.defaultPermit) {
            super.checkAccess(g);
        }
    }

    @Override
    public void checkAwtEventQueueAccess() {

        if(!this.defaultPermit) {
            super.checkAwtEventQueueAccess();
        }
    }

    @Override
    public void checkConnect(String host, int port, Object context) {

        if(!this.defaultPermit) {
            super.checkConnect(host, port, context);
        }
    }

    @Override
    public void checkConnect(String host, int port) {

        if(!this.defaultPermit) {
            super.checkConnect(host, port);
        }
    }

    @Override
    public void checkCreateClassLoader() {
        System.out.println("Creating classloader...");

        if(this.isInIsolatedGroup()) {
            throw new SecurityException("Isolated code cannot create classloaders");
        }
        
        if(!this.defaultPermit) {
            super.checkCreateClassLoader();
        }
    }

    @Override
    public void checkDelete(String file) {
        
        if(!this.defaultPermit) {
            super.checkDelete(file);
        }
    }

    @Override
    public void checkExec(String cmd) {
        
        if(!this.defaultPermit) {
            super.checkExec(cmd);
        }
    }

    @Override
    public void checkExit(int status) {
        
        if(this.isInIsolatedGroup()) {
            throw new SecurityException("Isolated code cannot exit JVM");
        }
        
        if(!this.defaultPermit) {
            super.checkExit(status);
        }
    }

    @Override
    public void checkLink(String lib) {
        
        if(!this.defaultPermit) {
            super.checkLink(lib);
        }
    }

    @Override
    public void checkListen(int port) {
        
        if(!this.defaultPermit) {
            super.checkListen(port);
        }
    }

    @Override
    public void checkMemberAccess(Class<?> arg0, int arg1) {
        System.out.printf("member access on %s @ %d\n", arg0.getName(), arg1);
        if(!this.defaultPermit) {
            super.checkMemberAccess(arg0, arg1);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void checkMulticast(InetAddress maddr, byte ttl) {
        
        if(!this.defaultPermit) {
            super.checkMulticast(maddr, ttl);
        }
    }

    @Override
    public void checkMulticast(InetAddress maddr) {
        
        if(!this.defaultPermit) {
            super.checkMulticast(maddr);
        }
    }

    @Override
    public void checkPackageAccess(String arg0) {
        
        if(!this.defaultPermit) {
            super.checkPackageAccess(arg0);
        }
    }

    @Override
    public void checkPackageDefinition(String arg0) {
        
        if(!this.defaultPermit) {
            super.checkPackageDefinition(arg0);
        }
    }

    @Override
    public void checkPermission(Permission perm, Object context) {
        
        //System.out.printf("Check perm: %s -> %s\n", perm, context);
        
        if(!this.defaultPermit) {
            super.checkPermission(perm, context);
        }
    }

    @Override
    public void checkPermission(Permission perm) {
        
        //System.out.printf("Check perm: %s (name: %s, action: %s, class: %s)\n", perm, perm.getName(), perm.getActions(), perm.getClass().getName());

        // don't allow certain things or otherwise intercept
        if(this.isInIsolatedGroup()) {
            if(perm instanceof java.lang.RuntimePermission) {
                if("getClassLoader".equals(perm.getName())) {
                    
                    // root isolated thread can, inner isolated thread can't
                    if(this.isIsolatedThread()) {
                        throw new SecurityException("Isolated code cannot get a classloader outside of the isolated implementation");
                    }
                }
            }
        }
        
        if(!this.defaultPermit) {
            super.checkPermission(perm);
        }
    }

    @Override
    public void checkPrintJobAccess() {
        
        if(!this.defaultPermit) {
            super.checkPrintJobAccess();
        }
    }

    @Override
    public void checkPropertiesAccess() {
        
        if(!this.defaultPermit) {
            super.checkPropertiesAccess();
        }
    }

    @Override
    public void checkPropertyAccess(String key) {
        
        if(!this.defaultPermit) {
            super.checkPropertyAccess(key);
        }
    }

    @Override
    public void checkRead(FileDescriptor fd) {
        
        if(!this.defaultPermit) {
            super.checkRead(fd);
        }
    }

    @Override
    public void checkRead(String file, Object context) {
        
        if(!this.defaultPermit) {
            super.checkRead(file, context);
        }
    }

    @Override
    public void checkRead(String file) {
        
        if(!this.defaultPermit) {
            super.checkRead(file);
        }
    }

    @Override
    public void checkSecurityAccess(String target) {
        
        System.out.printf("Check security: %s\n", target);
        
        if(!this.defaultPermit) {
            super.checkSecurityAccess(target);
        }
    }

    @Override
    public void checkSetFactory() {
        
        if(!this.defaultPermit) {
            super.checkSetFactory();
        }
    }

    @Override
    public void checkSystemClipboardAccess() {
        
        if(!this.defaultPermit) {
            super.checkSystemClipboardAccess();
        }
    }

    @Override
    public boolean checkTopLevelWindow(Object arg0) {
        
        if(!this.defaultPermit) {
            return super.checkTopLevelWindow(arg0);
        }
        
        // only allow when not in the isolated group
        return !this.isInIsolatedGroup();
    }

    @Override
    public void checkWrite(FileDescriptor fd) {
        
        if(!this.defaultPermit) {
            super.checkWrite(fd);
        }
    }

    @Override
    public void checkWrite(String file) {
        
        if(!this.defaultPermit) {
            super.checkWrite(file);
        }
    }
    
    private boolean isIsolatedThread() {
        Thread current = Thread.currentThread();
        return this.isInstanceOf(current.getClass(), IsolatedThread.class);
    }
    
    /*
    private boolean isRootIsolatedThread() {
        Thread current = Thread.currentThread();
        return this.isInstanceOf(current.getClass(), IsolatedThread.class);
    }
    */
    
    /**
     * Checks instances spanning across classloaders because
     * this can cause real problems
     * 
     * @param target
     * @param checkAgainst
     * @return
     */
    private boolean isInstanceOf(Class<?> target, Class<?> checkAgainst) {
        // double null
        if(target == null && checkAgainst == null) {
            return true;
        }
        
        // make sure not null
        if(target == null || checkAgainst == null) {
            return false;
        }
        
        // simple check
        return target.equals(checkAgainst) || target.getName().equals(checkAgainst.getName());
    }
    
    private boolean isInIsolatedGroup() {
        // get current group
        ThreadGroup current = this.getThreadGroup();
        
        // while a group is available, check group
        while(current != null) {
            // if the current thread group has children
            if(IsolationGroup.ISOLATION_GROUP_NAME.equals(current.getName())
            || current instanceof IsolationGroup) {
                return true;
            }
            
            // check up the tree, looking for the root
            current = current.getParent();
        }
        return false;
    }
}
