package io.github.PlatovD.svarog.core.testbeans;

public class LifecycleBean {

    public boolean afterCreateCalled = false;
    public boolean beforeDestroyCalled = false;

    public void afterCreate() {
        this.afterCreateCalled = true;
    }

    public void beforeDestroy() {
        this.beforeDestroyCalled = true;
    }
}