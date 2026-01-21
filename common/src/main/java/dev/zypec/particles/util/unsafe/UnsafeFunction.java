package dev.zypec.particles.util.unsafe;

import dev.zypec.particles.effect.exception.ReaderException;

@FunctionalInterface
public interface UnsafeFunction<T, R> {
    R apply(T t) throws ReaderException;
}