package io.github.PlatovD.svarog.core.testbeans;

public class FailingLifecycleBean {

    public void afterCreate() {
        throw new RuntimeException("boom");
    }
}