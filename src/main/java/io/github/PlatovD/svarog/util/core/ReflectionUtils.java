package io.github.PlatovD.svarog.util.core;

import io.github.PlatovD.svarog.exception.SvarogException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectionUtils {

    private ReflectionUtils() {
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new SvarogException(
                    "Class " + clazz.getName() + " has no no-arg constructor", e);
        } catch (InstantiationException e) {
            throw new SvarogException(
                    "Cannot instantiate " + clazz.getName() +
                            " (abstract or interface?)", e);
        } catch (IllegalAccessException e) {
            throw new SvarogException(
                    "Cannot access constructor of " + clazz.getName(), e);
        } catch (InvocationTargetException e) {
            throw new SvarogException(
                    "Constructor of " + clazz.getName() + " threw exception",
                    e.getCause());
        }
    }

    public static void setField(Object target, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new SvarogException(
                    "Cannot set field " + field.getName() +
                            " on " + target.getClass().getName(), e);
        }
    }

    public static Object getField(Object target, Field field) {
        try {
            field.setAccessible(true);
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new SvarogException(
                    "Cannot read field " + field.getName() +
                            " on " + target.getClass().getName(), e);
        }
    }

    public static void invokeMethod(Object target, Method method) {
        try {
            method.setAccessible(true);
            method.invoke(target);
        } catch (IllegalAccessException e) {
            throw new SvarogException(
                    "Cannot invoke method " + method.getName() +
                            " on " + target.getClass().getName(), e);
        } catch (InvocationTargetException e) {
            throw new SvarogException(
                    "Method " + method.getName() +
                            " on " + target.getClass().getName() +
                            " threw exception", e.getCause());
        }
    }
}
