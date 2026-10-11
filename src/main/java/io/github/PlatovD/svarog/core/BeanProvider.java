package io.github.PlatovD.svarog.core;

public interface BeanProvider {

    <T> T getBean(Class<T> type);

    Object getBean(String name);
}