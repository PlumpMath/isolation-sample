package com.chrisruffalo.isolation;

public class IsolationGroup extends ThreadGroup {

    public static final String ISOLATION_GROUP_NAME = "isolated";
    
    private static IsolationGroup instance = null;
    
    public static synchronized IsolationGroup get() {
        if(IsolationGroup.instance == null) {
            IsolationGroup.instance = new IsolationGroup();
        }
        return IsolationGroup.instance;
    }
    
    private IsolationGroup() {
        super(IsolationGroup.ISOLATION_GROUP_NAME);
    }
    
}
