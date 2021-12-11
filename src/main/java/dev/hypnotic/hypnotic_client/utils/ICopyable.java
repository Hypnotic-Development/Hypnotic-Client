package dev.hypnotic.hypnotic_client.utils;

public interface ICopyable<T extends ICopyable<T>> {
    T set(T value);

    T copy();
}
