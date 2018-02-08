package com.mmxlabs.common.util;

@FunctionalInterface
public interface ToBooleanFunction<T> {
	boolean accept(T t);
}
