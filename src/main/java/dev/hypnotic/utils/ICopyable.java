package dev.hypnotic.utils;

public interface ICopyable<T extends ICopyable<T>> {
    T set(T value);

    T copy();
}
