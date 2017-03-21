package com.mmxlabs.common.util;

@FunctionalInterface
public interface ToLongTriFunction<T, U, V> {

	long applyAsLong(T t, U u, V v);
}